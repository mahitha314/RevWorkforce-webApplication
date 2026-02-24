package com.workforce.repository;

import com.workforce.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository 
        extends JpaRepository<Department, Long> {

}