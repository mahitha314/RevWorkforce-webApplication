package com.revworkforce.adminserviceImpl;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.revworkforce.adminservice.EmployeeManagementService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.EmployeeDTO;
import com.revworkforce.model.Department;
import com.revworkforce.model.Designation;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.DepartmentRepository;
import com.revworkforce.repository.DesignationRepository;
import com.revworkforce.repository.EmployeeRepository;

@Service
public class EmployeeManagementServiceImpl implements EmployeeManagementService {
	private final EmployeeRepository employeeRepository;
	private final DepartmentRepository departmentRepository;
	private final DesignationRepository designationRepository;
	private final PasswordEncoder passwordEncoder;

	public EmployeeManagementServiceImpl(EmployeeRepository employeeRepository,
			DepartmentRepository departmentRepository, DesignationRepository designationRepository,
			PasswordEncoder passwordEncoder) {
		this.employeeRepository = employeeRepository;
		this.departmentRepository = departmentRepository;
		this.designationRepository = designationRepository;
		this.passwordEncoder = passwordEncoder;
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

		return new ResponseEntity<>(new ApiResponse(201, "Employee added successfully", null), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ApiResponse> getAllEmployees() {

		List<Employee> employees = employeeRepository.findAll();

		return new ResponseEntity<>(new ApiResponse(200, "Employees fetched successfully", employees), HttpStatus.OK);
	}

}