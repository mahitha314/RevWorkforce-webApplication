package com.revworkforce.adminserviceImpl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.revworkforce.adminservice.AdminService;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    public AdminServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
        logger.info("AdminServiceImpl initialized");
    }

    @Override
    public Optional<Employee> getEmployeeByEmail(String email) {
        logger.info("Fetching employee by email: {}", email);

        Optional<Employee> employee = employeeRepository.findByEmail(email);

        if (employee.isPresent()) {
            logger.debug("Employee found for email: {}", email);
        } else {
            logger.warn("No employee found for email: {}", email);
        }

        return employee;
    }

    @Override
    public long getTotalEmployees() {
        logger.info("Request received to get total employees");
        return 0;
    }

    @Override
    public long getTotalManagers() {
        logger.info("Request received to get total managers");
        return 0;
    }

    @Override
    public long getTotalRegularEmployees() {
        logger.info("Request received to get total regular employees");
        return 0;
    }

    @Override
    public long getPendingLeaves() {
        logger.info("Request received to get pending leaves count");
        return 0;
    }

    @Override
    public long getApprovedLeaves() {
        logger.info("Request received to get approved leaves count");
        return 0;
    }
}