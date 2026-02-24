package com.revworkforce.adminservice;

import org.springframework.http.ResponseEntity;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.EmployeeDTO;

public interface EmployeeManagementService {

	ResponseEntity<ApiResponse> addEmployee(EmployeeDTO dto);
    ResponseEntity<ApiResponse> getAllEmployees();
	
}