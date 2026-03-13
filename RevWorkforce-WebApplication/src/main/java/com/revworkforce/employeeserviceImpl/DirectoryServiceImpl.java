package com.revworkforce.employeeserviceImpl;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.employeeservice.DirectoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectoryServiceImpl implements DirectoryService {

    private static final Logger logger =
            LoggerFactory.getLogger(DirectoryServiceImpl.class);

    private final EmployeeRepository employeeRepo;

    public DirectoryServiceImpl(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
        logger.info("DirectoryServiceImpl initialized");
    }

    @Override
    public ApiResponse getAllEmployees() {

        logger.info("Fetching all employees from directory");

        List<Employee> employees = employeeRepo.findAll();

        logger.debug("Total employees fetched: {}", employees.size());

        return new ApiResponse(
                200,
                "Employees fetched successfully",
                employees
        );
    }

    @Override
    public ApiResponse searchEmployees(String keyword) {

        logger.info("Searching employees with keyword: {}", keyword);

        List<Employee> employees =
                employeeRepo
                .findByEmployeeIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword, keyword, keyword, keyword);

        logger.debug("Search results count: {}", employees.size());

        return new ApiResponse(
                200,
                "Search results fetched successfully",
                employees
        );
    }
}