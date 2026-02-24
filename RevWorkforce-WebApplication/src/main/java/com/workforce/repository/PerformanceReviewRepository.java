package com.workforce.repository;

import com.workforce.model.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {

   
    List<PerformanceReview> findByEmployeeEmployeeId(Long employeeId);
}