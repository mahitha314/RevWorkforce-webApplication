package com.revworkforce.employeeserviceImpl;

import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.employeeservice.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger =
            LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepo;

    public EmployeeServiceImpl(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
        logger.info("EmployeeServiceImpl initialized");
    }

    @Override
    public Optional<Employee> findByEmail(String email) {

        logger.info("Fetching employee by email: {}", email);

        Optional<Employee> employee = employeeRepo.findByEmail(email);

        if (employee.isPresent()) {
            logger.debug("Employee found for email: {}", email);
        } else {
            logger.warn("No employee found for email: {}", email);
        }

        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {

        logger.info("Fetching all employees");

        List<Employee> employees = employeeRepo.findAll();

        logger.debug("Total employees fetched: {}", employees.size());

        return employees;
    }

    @Override
    public List<Employee> searchEmployees(String keyword) {

        logger.info("Searching employees with keyword: {}", keyword);

        if (keyword == null || keyword.trim().isEmpty()) {

            logger.debug("Keyword empty, returning all employees");

            List<Employee> employees = employeeRepo.findAll();

            logger.debug("Total employees returned: {}", employees.size());

            return employees;
        }

        String[] parts = keyword.trim().split(" ");

        if (parts.length == 2) {

            logger.debug("Searching by firstName and lastName");

            List<Employee> employees =
                    employeeRepo
                    .findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
                            parts[0], parts[1]);

            logger.debug("Search results count: {}", employees.size());

            return employees;
        }

        logger.debug("Searching by firstName OR lastName OR email");

        List<Employee> employees =
                employeeRepo
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword, keyword, keyword);

        logger.debug("Search results count: {}", employees.size());

        return employees;
    }
}