package com.revworkforce.adminserviceImpl;

import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.revworkforce.adminservice.AdminService;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;

@Service
public class AdminServiceImpl implements AdminService {

	private static final Logger logger = LogManager.getLogger(AdminServiceImpl.class);

	private final EmployeeRepository employeeRepository;

	public AdminServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public Optional<Employee> getEmployeeByEmail(String email) {

		logger.info("Fetching employee by email: {}", email);

		Optional<Employee> employee = employeeRepository.findByEmail(email);

		if (employee.isPresent()) {
			logger.debug("Employee found for email {}", email);
		} else {
			logger.warn("Employee not found for email {}", email);
		}

		return employee;
	}

	@Override
	public long getTotalEmployees() {

		logger.info("Fetching total number of employees");

		long count = employeeRepository.count();

		logger.debug("Total employees count: {}", count);

		return count;
	}

	@Override
	public long getTotalManagers() {

		logger.info("Fetching total number of managers");

		long count = employeeRepository.countByRole("MANAGER");

		logger.debug("Total managers count: {}", count);

		return count;
	}

	@Override
	public long getTotalRegularEmployees() {

		logger.info("Fetching total number of regular employees");

		long count = employeeRepository.countByRole("EMPLOYEE");

		logger.debug("Total regular employees count: {}", count);

		return count;
	}

	@Override
	public long getPendingLeaves() {

		return 0;
	}

	@Override
	public long getApprovedLeaves() {

		return 0;
	}

}