package com.revworkforce.admincontroller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.revworkforce.adminservice.EmployeeManagementService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.EmployeeDTO;
import com.revworkforce.model.Department;
import com.revworkforce.model.Designation;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.DepartmentRepository;
import com.revworkforce.repository.DesignationRepository;
import com.revworkforce.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class EmployeeManagementController {

	private final EmployeeManagementService employeeManagementService;
	private final DesignationRepository designationRepository;
	private final DepartmentRepository departmentRepository;
	private final EmployeeRepository employeeRepository;

	public EmployeeManagementController(EmployeeManagementService employeeManagementService,
			DesignationRepository designationRepository, DepartmentRepository departmentRepository,
			EmployeeRepository employeeRepository) {

		this.employeeManagementService = employeeManagementService;
		this.designationRepository = designationRepository;
		this.departmentRepository = departmentRepository;
		this.employeeRepository = employeeRepository;
	}

	@PutMapping("/employees/{employeeId}")
	public ResponseEntity<ApiResponse> updateEmployee(@PathVariable String employeeId, @RequestBody EmployeeDTO dto) {

		return employeeManagementService.updateEmployee(employeeId, dto);
	}

	@DeleteMapping("/employees/{employeeId}")
	public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable String employeeId) {

		return employeeManagementService.deleteEmployee(employeeId);
	}

	@GetMapping("/departments")
	public ResponseEntity<ApiResponse> getAllDepartments() {

		List<Department> departments = departmentRepository.findAll();

		return ResponseEntity.ok(new ApiResponse(200, "Departments fetched successfully", departments));
	}

	@GetMapping("/designations/by-department/{deptId}")
	public ResponseEntity<ApiResponse> getDesignationsByDepartment(@PathVariable Long deptId) {

		List<Designation> designations = designationRepository.findByDepartmentId(deptId);

		return ResponseEntity.ok(new ApiResponse(200, "Designations fetched successfully", designations));
	}

	@GetMapping("/managers")
	public ResponseEntity<ApiResponse> getManagers() {

		List<Employee> managers = employeeManagementService.getManagers();

		return ResponseEntity.ok(new ApiResponse(200, "Managers fetched successfully", managers));
	}

	@PostMapping("/employees")
	public ResponseEntity<ApiResponse> addEmployee(@RequestBody EmployeeDTO dto) {

		return employeeManagementService.addEmployee(dto);
	}

	@GetMapping("/employees")
	public ResponseEntity<ApiResponse> getAllEmployees() {

		return employeeManagementService.getAllEmployees();
	}

	@GetMapping("/employees/{employeeId}")
	public ResponseEntity<ApiResponse> getEmployeeById(@PathVariable String employeeId) {

		return employeeManagementService.getByEmployeeId(employeeId);
	}

	@GetMapping("/employees/search")
	public ResponseEntity<ApiResponse> searchEmployees(@RequestParam("q") String query) {

		return employeeManagementService.searchEmployees(query);
	}

	@PutMapping("/employees/{employeeId}/deactivate")
	public ResponseEntity<ApiResponse> deactivateEmployee(@PathVariable String employeeId,
			@RequestParam(required = false) String reason) {

		return employeeManagementService.deactivateEmployee(employeeId, reason);
	}

	@PutMapping("/employees/{employeeId}/reactivate")
	public ResponseEntity<ApiResponse> reactivateEmployee(@PathVariable String employeeId,
			@RequestParam(required = false) String reason) {

		return employeeManagementService.reactivateEmployee(employeeId, reason);
	}

	@PutMapping("/employees/{employeeId}/manager/{managerId}")
	public ResponseEntity<ApiResponse> changeManager(@PathVariable String employeeId, @PathVariable Long managerId) {

		return employeeManagementService.changeManager(employeeId, managerId);
	}

}