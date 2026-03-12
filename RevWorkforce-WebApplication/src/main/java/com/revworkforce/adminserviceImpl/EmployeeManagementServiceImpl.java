package com.revworkforce.adminserviceImpl;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.adminservice.EmployeeManagementService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.EmployeeDTO;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.model.Department;
import com.revworkforce.model.Designation;
import com.revworkforce.model.Employee;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.DepartmentRepository;
import com.revworkforce.repository.DesignationRepository;
import com.revworkforce.repository.EmployeeRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class EmployeeManagementServiceImpl implements EmployeeManagementService {

	private final EmployeeRepository employeeRepository;
	private final DepartmentRepository departmentRepository;
	private final DesignationRepository designationRepository;
	private final PasswordEncoder passwordEncoder;
	private final ActivityLogService activityLogService;
	private final NotificationService notificationService;
	private final HttpServletRequest request;

	public EmployeeManagementServiceImpl(EmployeeRepository employeeRepository,
			DepartmentRepository departmentRepository, DesignationRepository designationRepository,
			ActivityLogService activityLogService, PasswordEncoder passwordEncoder,
			NotificationService notificationService, HttpServletRequest request) {

		this.employeeRepository = employeeRepository;
		this.departmentRepository = departmentRepository;
		this.designationRepository = designationRepository;
		this.passwordEncoder = passwordEncoder;
		this.activityLogService = activityLogService;
		this.notificationService = notificationService;
		this.request = request;

	}

	@Override
	public ResponseEntity<ApiResponse> addEmployee(EmployeeDTO dto) {

		if (employeeRepository.findByEmail(dto.getEmail()).isPresent()) {
			return new ResponseEntity<>(new ApiResponse(409, "Email already exists", null), HttpStatus.CONFLICT);
		}

		if (employeeRepository.findByEmployeeId(dto.getEmployeeId()).isPresent()) {
			return new ResponseEntity<>(new ApiResponse(409, "Employee ID already exists", null), HttpStatus.CONFLICT);
		}

		Department department = departmentRepository.findById(dto.getDepartmentId())
				.orElseThrow(() -> new RuntimeException("Department not found"));

		Designation designation = designationRepository.findById(dto.getDesignationId())
				.orElseThrow(() -> new RuntimeException("Designation not found"));

		Employee manager = null;
		if (dto.getManagerId() != null) {
			manager = employeeRepository.findById(dto.getManagerId())
					.orElseThrow(() -> new RuntimeException("Manager not found"));
		}

		Employee employee = new Employee();
		employee.setEmployeeId(dto.getEmployeeId());
		employee.setFirstName(dto.getFirstName());
		employee.setLastName(dto.getLastName());
		employee.setEmail(dto.getEmail());
		employee.setPassword(passwordEncoder.encode(dto.getPassword()));
		employee.setPhoneNumber(dto.getPhoneNumber());
		employee.setAddress(dto.getAddress());
		employee.setEmergencyContact(dto.getEmergencyContact());
		employee.setRole(dto.getRole());
		employee.setStatus("ACTIVE");
		employee.setSalary(dto.getSalary());
		employee.setJoiningDate(LocalDate.now());
		employee.setDepartment(department);
		employee.setDesignation(designation);
		employee.setManager(manager);

		employeeRepository.save(employee);

		NotificationDTO notification = new NotificationDTO();
		notification.setEmployeeId(employee.getEmployeeId());
		notification.setTitle("Welcome to Company");
		notification.setMessage("Your account has been created successfully.");
		notification.setType("SYSTEM");
		notification.setStatus("ACTIVE");

		notificationService.createNotification(notification);
		activityLogService.log("Added Employee", "Employee Management", "Added employee " + employee.getFirstName(),
				"SUCCESS", request);

		return new ResponseEntity<>(new ApiResponse(201, "Employee added successfully", employee), HttpStatus.CREATED);

	}

	@Override
	public ResponseEntity<ApiResponse> getAllEmployees() {

		List<Employee> employees = employeeRepository.findAll();
		return new ResponseEntity<>(new ApiResponse(200, "Employees fetched successfully", employees), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ApiResponse> getByEmployeeId(String employeeId) {

		Employee employee = employeeRepository.findByEmployeeId(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found"));

		return new ResponseEntity<>(new ApiResponse(200, "Employee fetched successfully", employee), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ApiResponse> searchEmployees(String q) {
		List<Employee> employees = employeeRepository.search(q);

		activityLogService.log("Search Employee", "Employee Management", "Searched employees with query: " + q,
				"SUCCESS", request);

		return new ResponseEntity<>(new ApiResponse(200, "Search results", employees), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ApiResponse> updateEmployee(String employeeId, EmployeeDTO dto) {

		Employee existing = employeeRepository.findByEmployeeId(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found"));

		existing.setFirstName(dto.getFirstName());
		existing.setLastName(dto.getLastName());
		existing.setEmail(dto.getEmail());
		existing.setPhoneNumber(dto.getPhoneNumber());
		existing.setAddress(dto.getAddress());
		existing.setEmergencyContact(dto.getEmergencyContact());
		existing.setSalary(dto.getSalary());
		existing.setRole(dto.getRole());

		Department dept = departmentRepository.findById(dto.getDepartmentId())
				.orElseThrow(() -> new RuntimeException("Department not found"));
		existing.setDepartment(dept);

		Designation des = designationRepository.findById(dto.getDesignationId())
				.orElseThrow(() -> new RuntimeException("Designation not found"));
		existing.setDesignation(des);

		employeeRepository.save(existing);

		activityLogService.log("Updated Employee", "Employee Management", "Updated employee " + existing.getFirstName(),
				"SUCCESS", request);

		return ResponseEntity.ok(new ApiResponse(200, "Employee updated successfully", null));
	}

	@Override
	public ResponseEntity<ApiResponse> deleteEmployee(String employeeId) {

		Employee existing = employeeRepository.findByEmployeeId(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found"));

		employeeRepository.delete(existing);

		activityLogService.log("Deleted Employee", "Employee Management", "Deleted employee " + existing.getFirstName(),
				"SUCCESS", request);

		return ResponseEntity.ok(new ApiResponse(200, "Employee deleted successfully", null));
	}

	@Override
	public ResponseEntity<ApiResponse> reactivateEmployee(String employeeId, String reason) {

		Employee employee = employeeRepository.findByEmployeeId(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found"));

		employee.setStatus("ACTIVE");
		employeeRepository.save(employee);

		NotificationDTO notification = new NotificationDTO();
		notification.setEmployeeId(employeeId);
		notification.setTitle("Account Reactivated");
		notification.setMessage("Your account has been reactivated.");
		notification.setType("SYSTEM");
		notification.setStatus("ACTIVE");

		notificationService.createNotification(notification);

		activityLogService.log("Reactivated Employee", "Employee Management",
				"Reactivated employee " + employee.getFirstName(), "SUCCESS", request);

		return ResponseEntity.ok(new ApiResponse(200, "Employee reactivated successfully", null));
	}

	@Override
	public ResponseEntity<ApiResponse> deactivateEmployee(String employeeId, String reason) {

		Employee employee = employeeRepository.findByEmployeeId(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found"));

		employee.setStatus("INACTIVE");
		employeeRepository.save(employee);

		NotificationDTO notification = new NotificationDTO();
		notification.setEmployeeId(employeeId);
		notification.setTitle("Account Deactivated");
		notification.setMessage("Your account has been deactivated. Reason: " + reason);
		notification.setType("SYSTEM");
		notification.setStatus("ACTIVE");

		notificationService.createNotification(notification);

		activityLogService.log("Deactivated Employee", "Employee Management",
				"Deactivated employee " + employee.getFirstName(), "SUCCESS", request);

		return ResponseEntity.ok(new ApiResponse(200, "Employee deactivated successfully", null));
	}

	@Override
	public ResponseEntity<ApiResponse> changeManager(String employeeId, Long managerId) {

		Employee employee = employeeRepository.findByEmployeeId(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found"));

		Employee manager = employeeRepository.findById(managerId)
				.orElseThrow(() -> new RuntimeException("Manager not found"));

		employee.setManager(manager);
		employeeRepository.save(employee);

		activityLogService.log("Changed Manager", "Employee Management",
				"Changed manager for employee " + employee.getFirstName(), "SUCCESS", request);

		return new ResponseEntity<>(new ApiResponse(200, "Manager updated successfully", employee), HttpStatus.OK);
	}

	@Override
	public List<Employee> getManagers() {
		return employeeRepository.findByRole("MANAGER");
	}

	@Override
	public long countEmployees() {
		return employeeRepository.count();
	}

	@Override
	public long countManagers() {
		return employeeRepository.countByRole("MANAGER");
	}

}