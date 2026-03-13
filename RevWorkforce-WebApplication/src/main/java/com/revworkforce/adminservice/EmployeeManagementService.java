package com.revworkforce.adminservice;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.EmployeeDTO;
import com.revworkforce.model.Employee;

public interface EmployeeManagementService {

	ResponseEntity<ApiResponse> addEmployee(EmployeeDTO dto);

	ResponseEntity<ApiResponse> getAllEmployees();

	ResponseEntity<ApiResponse> getByEmployeeId(String employeeId);

	ResponseEntity<ApiResponse> searchEmployees(String q);

	ResponseEntity<ApiResponse> updateEmployee(String employeeId, EmployeeDTO dto);

	ResponseEntity<ApiResponse> changeManager(String employeeId, Long managerId);

	ResponseEntity<ApiResponse> deactivateEmployee(String employeeId, String reason);

	ResponseEntity<ApiResponse> reactivateEmployee(String employeeId, String reason);

	ResponseEntity<ApiResponse> deleteEmployee(String employeeId);

	List<Employee> getManagers();

	long countEmployees();

	long countManagers();

}