package com.workforce.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.workforce.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);
    
}