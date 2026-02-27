package com.revworkforce.managercontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.PerformanceReviewDTO;
import com.revworkforce.managerservice.PerformanceReviewService;

@RestController
@RequestMapping("/manager/performance")
public class PerformanceReviewController {

	private final PerformanceReviewService service;

	public PerformanceReviewController(PerformanceReviewService service) {
		this.service = service;
	}

	@GetMapping("/{managerId}")
	public ResponseEntity<ApiResponse> getReviews(@PathVariable Long managerId) {

		ApiResponse response = service.getTeamPerformanceReviews(managerId);

		return ResponseEntity.status(response.getStatus()).body(response);
	}

	@PostMapping("/review/{managerId}")
	public ResponseEntity<ApiResponse> submitReview(@PathVariable Long managerId,
			@RequestBody PerformanceReviewDTO dto) {

		ApiResponse response = service.submitManagerFeedback(managerId, dto);

		return ResponseEntity.status(response.getStatus()).body(response);
	}

}