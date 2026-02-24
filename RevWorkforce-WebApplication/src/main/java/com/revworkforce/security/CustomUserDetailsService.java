package com.revworkforce.security;

import org.springframework.security.core.userdetails.*;
<<<<<<< HEAD
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;

//import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
=======
import org.springframework.stereotype.Service;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final EmployeeRepository employeeRepository;
	
	public CustomUserDetailsService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}
>>>>>>> dev

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

<<<<<<< HEAD
        return org.springframework.security.core.userdetails.User.builder()
                .username(employee.getEmail())
                .password(employee.getPassword())
                .roles(employee.getRole())  
                .disabled(!employee.isActive())
                .build();
    }
=======
        return new CustomUserDetails(employee);
    }
    
>>>>>>> dev
}