package com.revworkforce.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.revworkforce.model.Goal;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    
	List<Goal> findByEmployee_Manager_Id(Long managerId);

    List<Goal> findByEmployee_Id(Long employeeId);
    
}