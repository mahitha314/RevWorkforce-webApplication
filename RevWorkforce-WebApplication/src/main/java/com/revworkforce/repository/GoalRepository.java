package com.revworkforce.repository;

<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revworkforce.model.Goal;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByEmployeeEmployeeId(Long employeeId);
}
=======
public interface GoalRepository {

}
>>>>>>> dev
