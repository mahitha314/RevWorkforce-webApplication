package com.workforce.security;

import com.workforce.model.Employee;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.*;

public class CustomUserDetails implements UserDetails {

    private Employee employee;

    public CustomUserDetails (Employee employee) {
        this.employee = employee;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + employee.getRole()));
    }

    @Override
    public String getPassword() { 
    	return employee.getPassword(); 
    }

    @Override
    public String getUsername() { 
    	return employee.getEmail(); 
    }

    @Override
    public boolean isAccountNonExpired() { 
    	return true; 
    }

    @Override
    public boolean isAccountNonLocked() { 
    	return true; 
    }

    @Override
    public boolean isCredentialsNonExpired(){ 
    	return true; 
    }

    @Override
    public boolean isEnabled() { 
    	return employee.isActive();
    }

    public Employee getEmployee(){
        return employee;
    }
    
}