package com.revworkforce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revworkforce.model.Department;

@Repository
public interface DepartmentRepository 
        extends JpaRepository<Department, Long> {

}