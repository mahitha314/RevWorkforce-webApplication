package com.revworkforce.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.revworkforce.model.Department;
import com.revworkforce.model.Designation;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.DepartmentRepository;
import com.revworkforce.repository.DesignationRepository;
import com.revworkforce.repository.EmployeeRepository;

@Component
public class DataInitializer implements CommandLineRunner {

	private final EmployeeRepository employeeRepository;
	private final DepartmentRepository departmentRepository;
	private final DesignationRepository designationRepository;
	private final PasswordEncoder passwordEncoder;

	public DataInitializer(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository,
			DesignationRepository designationRepository, PasswordEncoder passwordEncoder) {
		this.employeeRepository = employeeRepository;
		this.departmentRepository = departmentRepository;
		this.designationRepository = designationRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println("DataInitializer Started...");

		if (employeeRepository.findByEmail("admin@rev.com").isPresent()) {
			System.out.println("DEFAULT ADMIN already exists.");
			return;
		}

		Department department = departmentRepository.findByName("IT").orElseGet(() -> {
			Department d = new Department();
			d.setName("IT");
			return departmentRepository.save(d);
		});

		Designation designation = designationRepository
				.findByTitleAndDepartmentId("Software Engineer", department.getId()).orElseGet(() -> {
					Designation des = new Designation();
					des.setTitle("Software Engineer");
					des.setDepartment(department);
					return designationRepository.save(des);
				});

		Employee admin = new Employee();
		admin.setEmployeeId("REV1001");
		admin.setFirstName("MAHITHA");
		admin.setLastName("SAI");
		admin.setEmail("admin@rev.com");
		admin.setPassword(passwordEncoder.encode("Admin@123"));
		admin.setPhoneNumber("9562387496");
		admin.setAddress("HYDERABAD");
		admin.setEmergencyContact("9987465423");
		admin.setRole("ADMIN");
		admin.setStatus("ACTIVE");
		admin.setSalary(100000.0);
		admin.setJoiningDate(java.time.LocalDate.now());
		admin.setDepartment(department);
		admin.setDesignation(designation);

		employeeRepository.save(admin);

		System.out.println("DEFAULT ADMIN CREATED");
	}
	
}