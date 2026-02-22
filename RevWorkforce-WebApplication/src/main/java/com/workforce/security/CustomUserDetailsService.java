package com.workforce.security;

import com.workforce.model.Employee;
import com.workforce.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User(
                employee.getEmail(),
                employee.getPassword(),
                employee.isActive(),
                true,
                true,
                true,
                Collections.singleton(() ->
                        "ROLE_" + employee.getRole()
                )
        );
    }
}