package com.revworkforce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.revworkforce.model.Employee;
import com.revworkforce.security.CustomUserDetails;
import com.revworkforce.security.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

	private final CustomUserDetailsService customUserDetailsService;

	public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
		this.customUserDetailsService = customUserDetailsService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())

				.authorizeHttpRequests(auth -> auth

						// ✅ Public pages
						.requestMatchers("/", "/login", "/auth/**", "/css/**", "/js/**").permitAll()

						// Notifications (REST)
						.requestMatchers("/notifications/**").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")

						// Notifications (UI)
						.requestMatchers("/ui/notifications/**").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
						
						// ✅ Role Based APIs
						.requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/manager/**").hasRole("MANAGER")
						.requestMatchers("/employee/**").hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")

						.anyRequest().authenticated())

				// ✅ Enable Session (IMPORTANT FIX)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

				// ✅ Use form login for Thymeleaf
				.formLogin(form -> form.loginPage("/login").successHandler((request, response, authentication) -> {

					var authorities = authentication.getAuthorities();

					if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

						response.sendRedirect("/admin/dashboard");
					}

				else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {

						response.sendRedirect("/manager/dashboard");
					}

				else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"))) {

						CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

						Employee employee = userDetails.getEmployee();

						response.sendRedirect("/employee/dashboard");
					}

				else {
						response.sendRedirect("/login?error");
					}
				}).permitAll())

				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll());

		return http.build();
	}

	// 🔐 Password Encoder
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 🔐 Authentication Provider
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);

		provider.setPasswordEncoder(passwordEncoder());

		return provider;
	}

	// 🔐 Authentication Manager
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}