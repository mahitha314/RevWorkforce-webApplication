package com.revworkforce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revworkforce.model.PerformanceReview;

import java.util.List;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {

   
    List<PerformanceReview> findByEmployeeEmployeeId(Long employeeId);
}