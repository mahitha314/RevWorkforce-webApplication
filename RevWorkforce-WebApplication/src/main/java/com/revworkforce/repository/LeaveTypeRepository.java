
package com.revworkforce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revworkforce.model.LeaveType;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long>{
	
	Optional<LeaveType> findByTypeName(String typeName);
	boolean existsByTypeName(String typeName);
	
}
