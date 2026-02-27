package com.revworkforce.admincontroller;

import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.revworkforce.adminservice.EmployeeManagementService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.EmployeeDTO;

@RestController
@RequestMapping("/admin")
public class EmployeeManagementController {
	
	private final EmployeeManagementService employeeManagementService;

	public EmployeeManagementController(EmployeeManagementService employeeManagementService) {
		this.employeeManagementService = employeeManagementService;
	}

	@PostMapping("/add-employee")
	public ResponseEntity<ApiResponse> addEmployee(@Validated @RequestBody EmployeeDTO dto) {
		return employeeManagementService.addEmployee(dto);
	}

	@GetMapping("/all-employees")
	public ResponseEntity<ResponseEntity<ApiResponse>> getAllEmployees() {
		return ResponseEntity.ok(employeeManagementService.getAllEmployees());
	}
	
}