package com.revworkforce.adminservice;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.EmployeeDTO;

public interface EmployeeManagementService {

	ApiResponse addEmployee(EmployeeDTO dto);
	
    ApiResponse getAllEmployees();
	
}