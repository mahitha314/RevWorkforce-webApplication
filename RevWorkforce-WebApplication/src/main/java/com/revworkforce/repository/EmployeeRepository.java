package com.revworkforce.repository;

<<<<<<< HEAD
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
=======
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.revworkforce.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByEmail(String email);
	Optional<Employee> findByEmployeeId(String employeeId);
	
>>>>>>> dev
}