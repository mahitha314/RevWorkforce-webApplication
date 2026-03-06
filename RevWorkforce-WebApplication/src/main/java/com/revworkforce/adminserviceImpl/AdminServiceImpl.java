package com.revworkforce.adminserviceImpl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revworkforce.adminservice.AdminService;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;

@Service
public class AdminServiceImpl implements AdminService {
	private final EmployeeRepository employeeRepository;

	public AdminServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public Optional<Employee> getEmployeeByEmail(String email) {
		return employeeRepository.findByEmail(email);
	}

	@Override
	public long getTotalEmployees() {

		return 0;
	}

	@Override
	public long getTotalManagers() {

		return 0;
	}

	@Override
	public long getTotalRegularEmployees() {

		return 0;
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
