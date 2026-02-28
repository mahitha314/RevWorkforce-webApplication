package com.revworkforce.adminserviceImpl;

import java.time.LocalDate;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

@Service
public class EmployeeManagementServiceImpl implements EmployeeManagementService {
	private final EmployeeRepository employeeRepository;
	private final DepartmentRepository departmentRepository;
	private final DesignationRepository designationRepository;
	private final PasswordEncoder passwordEncoder;
	private final NotificationService notificationService;

	public EmployeeManagementServiceImpl(EmployeeRepository employeeRepository,
			DepartmentRepository departmentRepository, DesignationRepository designationRepository,
			PasswordEncoder passwordEncoder,
	        NotificationService notificationService) {
		this.employeeRepository = employeeRepository;
		this.departmentRepository = departmentRepository;
		this.designationRepository = designationRepository;
		this.passwordEncoder = passwordEncoder;
		this.notificationService = notificationService;
	}

	@Override
	public ApiResponse addEmployee(EmployeeDTO dto) {

		if (employeeRepository.findByEmail(dto.getEmail()).isPresent()) {
			return new ApiResponse(409, "Email already exists", null);
		}

		if (employeeRepository.findByEmployeeId(dto.getEmployeeId()).isPresent()) {
			return new ApiResponse(409, "Employee ID already exists", null);
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

		employee.setRole(dto.getRole());
		employee.setDepartment(department);
		employee.setDesignation(designation);
		employee.setManager(manager);

		employee.setSalary(dto.getSalary());
		employee.setPhoneNumber(dto.getPhoneNumber());
		employee.setAddress(dto.getAddress());
		employee.setEmergencyContact(dto.getEmergencyContact());

		employee.setStatus("ACTIVE");
		employee.setJoiningDate(LocalDate.now());

		employeeRepository.save(employee);

		NotificationDTO notification = new NotificationDTO();
		notification.setEmployeeId(employee.getEmployeeId());
		notification.setTitle("Welcome to RevWorkForce");
		notification.setMessage("Your account has been created successfully. "
		        + "Please login using your credentials.");
		notification.setType("SYSTEM");
		notification.setStatus("ACTIVE");
		notification.setReferenceId(null);

		notificationService.createNotification(notification);

		if (manager != null) {
		    NotificationDTO managerNotification = new NotificationDTO();
		    managerNotification.setEmployeeId(manager.getEmployeeId());
		    managerNotification.setTitle("New Team Member Assigned");
		    managerNotification.setMessage(employee.getFirstName()
		            + " has been assigned under you.");
		    managerNotification.setType("SYSTEM");
		    managerNotification.setStatus("ACTIVE");
		    managerNotification.setReferenceId(null);

		    notificationService.createNotification(managerNotification);
		}

		return new ApiResponse(201, "Employee added successfully", null);
	}

	@Override
	public ApiResponse getAllEmployees() {
		List<Employee> employees = employeeRepository.findAll();
		return new ApiResponse(200, "Employees fetched successfully", employees);
	}

}