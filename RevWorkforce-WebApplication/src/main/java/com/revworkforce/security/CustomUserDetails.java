package com.revworkforce.security;

<<<<<<< HEAD
public class CustomUserDetails {

}
=======
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.revworkforce.model.Employee;

public class CustomUserDetails implements UserDetails {

	private final Employee employee;

	public CustomUserDetails(Employee employee) {
		this.employee = employee;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + employee.getRole()));
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
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return "ACTIVE".equals(employee.getStatus());
	}

}
>>>>>>> dev
