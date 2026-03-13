package com.revworkforce.admincontroller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final EmployeeRepository employeeRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public AuthController(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.employeeRepository = employeeRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}
	
	

	@PostMapping("/login")
	public Map<String, Object> login(@RequestBody Map<String, String> request) {

		String email = request.get("email");
		String password = request.get("password");

		Employee employee = employeeRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Invalid credentials"));

		if (!passwordEncoder.matches(password, employee.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}

		String token = jwtUtil.generateToken(employee.getEmail(), employee.getRole());

		Map<String, Object> response = new HashMap<>();
		response.put("token", token);
		response.put("role", employee.getRole());
		return response;

	}

}