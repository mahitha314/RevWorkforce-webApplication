package com.workforce.repository;

import com.workforce.model.Employee;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    long countByActiveTrue();
    long countByActiveFalse();

    @Modifying
    @Query("UPDATE Employee e SET e.active = true WHERE e.id = :id")
    void activateEmployeeById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Employee e SET e.active = false WHERE e.id = :id")
    void deactivateEmployeeById(@Param("id") Long id);
}