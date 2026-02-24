package com.revworkforce.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.revworkforce.model.Department;
import com.revworkforce.model.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    long countByActiveTrue();
    long countByActiveFalse();
    long countByDepartment(Department department);

    @Modifying
    @Query("UPDATE Employee e SET e.active = true WHERE e.id = :id")
    void activateEmployeeById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Employee e SET e.active = false WHERE e.id = :id")
    void deactivateEmployeeById(@Param("id") Long id);
}