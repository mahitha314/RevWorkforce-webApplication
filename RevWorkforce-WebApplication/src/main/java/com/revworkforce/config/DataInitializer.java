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

		if (employeeRepository.findByEmail("admin@rev.com").isEmpty()) {

			Department department = new Department();
			department.setName("IT");
			departmentRepository.save(department);

			Designation designation = new Designation();
			designation.setTitle("Software Engineer");
			designationRepository.save(designation);

			Employee admin = new Employee();
			admin.setEmployeeId("REV1001");
			admin.setFirstName("ADMIN");
			admin.setLastName("USER");
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

}