package com.revworkforce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.revworkforce.model.Designation;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long>{

	Optional<Designation> findByTitleAndDepartmentId(String title, Long departmentId);
	List<Designation> findByDepartmentId(Long departmentId);
}
