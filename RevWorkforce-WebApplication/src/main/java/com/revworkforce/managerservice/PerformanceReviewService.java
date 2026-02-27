package com.revworkforce.managerservice;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.PerformanceReviewDTO;

public interface PerformanceReviewService {

	ApiResponse getTeamPerformanceReviews(Long managerId);

	ApiResponse submitManagerFeedback(Long managerId, PerformanceReviewDTO dto);

}