package com.revworkforce.employeeserviceImpl;

import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.employeeservice.EmployeeService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepo;

    public EmployeeServiceImpl(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        return employeeRepo.findByEmail(email);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    @Override
    public List<Employee> searchEmployees(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return employeeRepo.findAll();
        }

        String[] parts = keyword.trim().split(" ");

        if (parts.length == 2) {
            return employeeRepo
                    .findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
                            parts[0], parts[1]);
        }

        return employeeRepo
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword, keyword, keyword);
    }
    
}