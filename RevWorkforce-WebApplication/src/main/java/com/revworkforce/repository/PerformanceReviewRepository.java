package com.revworkforce.repository;

import com.revworkforce.model.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {

	List<PerformanceReview> findByEmployee_Id(Long employeeId);
}