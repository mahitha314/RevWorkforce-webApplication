package com.workforce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.workforce.model.Employee;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByManager_Id(Long managerId);
}