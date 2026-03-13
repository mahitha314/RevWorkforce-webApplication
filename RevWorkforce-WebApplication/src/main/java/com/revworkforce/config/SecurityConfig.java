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
import com.revworkforce.util.JwtUtil;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/", "/login", "/auth/**", "/css/**", "/js/**").permitAll()

                .requestMatchers("/notifications/**").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                .requestMatchers("/ui/notifications/**").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")

                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/manager/**").hasRole("MANAGER")
                .requestMatchers("/employee/**").hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")

                .anyRequest().authenticated()
            )

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )

            .formLogin(form -> form
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {

                    CustomUserDetails userDetails =
                            (CustomUserDetails) authentication.getPrincipal();

                    Employee employee = userDetails.getEmployee();

                    String token = jwtUtil.generateToken(employee.getEmail(), employee.getRole());
                    request.getSession().setAttribute("JWT_TOKEN", token);

                    var authorities = authentication.getAuthorities();

                    if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                        response.sendRedirect("/admin/dashboard");
                    } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
                        response.sendRedirect("/manager/dashboard");
                    } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"))) {
                        response.sendRedirect("/employee/dashboard");
                    } else {
                        response.sendRedirect("/login?error");
                    }
                })
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}