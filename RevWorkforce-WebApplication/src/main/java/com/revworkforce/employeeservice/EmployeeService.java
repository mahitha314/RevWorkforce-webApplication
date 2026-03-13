package com.revworkforce.employeeservice;

import com.revworkforce.model.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {

	Optional<Employee> findByEmail(String email);

	List<Employee> getAllEmployees();

	List<Employee> searchEmployees(String keyword);
}