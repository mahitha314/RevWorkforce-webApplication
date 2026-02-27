package com.revworkforce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.revworkforce.security.JwtAuthFilter;

@Configuration
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;

	public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http

				.csrf(csrf -> csrf.disable())

				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.authorizeHttpRequests(auth -> auth

						.requestMatchers("/login").permitAll().requestMatchers("/auth/**").permitAll()
						.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

						.requestMatchers("/manager/dashboard", "/manager/performance_review", "/manager/team_structure",
								"/employee/**", "/admin/**")
						.permitAll()

						.requestMatchers("/manager/performance/**").hasAuthority("ROLE_MANAGER")

						.requestMatchers("/manager/team/**").hasAuthority("ROLE_MANAGER")

						.requestMatchers("/admin/api/**").hasAuthority("ROLE_ADMIN")

						.requestMatchers("/employee/api/**")
						.hasAnyAuthority("ROLE_EMPLOYEE", "ROLE_MANAGER", "ROLE_ADMIN")

						.anyRequest().authenticated())

				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

		return config.getAuthenticationManager();
	}
}