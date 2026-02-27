package com.revworkforce.config;

import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.security.JwtAuthFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final EmployeeRepository employeeRepository;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          EmployeeRepository employeeRepository) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.employeeRepository = employeeRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/employee/**")
                    .hasAnyRole("EMPLOYEE","MANAGER","ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/manager/**").hasRole("MANAGER")
                .anyRequest().authenticated()
            )

            // ✅ Dynamic redirect after login
            .formLogin(form -> form
                .successHandler((request, response, authentication) -> {

                    String email = authentication.getName();

                    Employee employee = employeeRepository
                            .findByEmail(email)
                            .orElseThrow();

//                    response.sendRedirect(
//                            "/employee/goals/" + employee.getId()
                    response.sendRedirect("/employee/dashboard");
                })
                .permitAll()
            )

            .logout(logout -> logout.permitAll())

            // Keep JWT for API
            .addFilterBefore(jwtAuthFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}