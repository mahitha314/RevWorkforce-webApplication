package com.revworkforce.employeeservice;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.PerformanceReviewDTO;

public interface PerformanceService {

	ApiResponse createSelfReview(Long employeeId, PerformanceReviewDTO dto);

	ApiResponse getEmployeeReviews(Long employeeId);

	ApiResponse submitReview(Long reviewId);

	ApiResponse deleteReview(Long reviewId);
	
}