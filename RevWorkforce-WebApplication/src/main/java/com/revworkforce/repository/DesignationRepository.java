package com.revworkforce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.revworkforce.model.Designation;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long>{

}