package com.revworkforce.employeeserviceImpl;

import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.employeeservice.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private static final Logger logger = LogManager.getLogger(EmployeeServiceImpl.class);

	private final EmployeeRepository employeeRepo;

	public EmployeeServiceImpl(EmployeeRepository employeeRepo) {
		this.employeeRepo = employeeRepo;
	}

	@Override
	public Optional<Employee> findByEmail(String email) {

		logger.info("Searching employee by email {}", email);

		Optional<Employee> employee = employeeRepo.findByEmail(email);

		if (employee.isPresent()) {
			logger.debug("Employee found for email {}", email);
		} else {
			logger.warn("Employee not found for email {}", email);
		}

		return employee;
	}

	@Override
	public List<Employee> getAllEmployees() {

		logger.info("Fetching all employees");

		List<Employee> employees = employeeRepo.findAll();

		logger.debug("Total employees fetched {}", employees.size());

		return employees;
	}

	@Override
	public List<Employee> searchEmployees(String keyword) {

		logger.info("Searching employees with keyword {}", keyword);

		if (keyword == null || keyword.trim().isEmpty()) {

			logger.debug("Keyword empty, returning all employees");

			return employeeRepo.findAll();
		}

		String[] parts = keyword.trim().split(" ");

		if (parts.length == 2) {

			logger.debug("Searching employees by first name {} and last name {}", parts[0], parts[1]);

			return employeeRepo.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(parts[0], parts[1]);
		}

		logger.debug("Searching employees by keyword {}", keyword);

		return employeeRepo
				.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword,
						keyword, keyword);
	}

}