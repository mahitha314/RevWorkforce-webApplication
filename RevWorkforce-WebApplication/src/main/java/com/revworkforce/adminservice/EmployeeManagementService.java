package com.revworkforce.adminservice;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.EmployeeDTO;
import com.revworkforce.model.Employee;

public interface EmployeeManagementService {

    // ================= CREATE =================
    ResponseEntity<ApiResponse> addEmployee(EmployeeDTO dto);

    // ================= READ =================
    ResponseEntity<ApiResponse> getAllEmployees();

    ResponseEntity<ApiResponse> getByEmployeeId(String employeeId);

    ResponseEntity<ApiResponse> searchEmployees(String q);

    // ================= UPDATE =================
    ResponseEntity<ApiResponse> updateEmployee(String employeeId, EmployeeDTO dto);

    ResponseEntity<ApiResponse> changeManager(String employeeId, Long managerId);

    // ================= STATUS =================
    ResponseEntity<ApiResponse> deactivateEmployee(String employeeId, String reason);

    ResponseEntity<ApiResponse> reactivateEmployee(String employeeId, String reason);

    // ================= DELETE =================
    ResponseEntity<ApiResponse> deleteEmployee(String employeeId);

    // ================= DROPDOWN =================
    List<Employee> getManagers();
    
 // ================= DASHBOARD COUNTS =================
    long countEmployees();
    long countManagers();
}