package com.workforce.managerservice;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workforce.dto.PerformanceReviewDTO;
import com.workforce.model.PerformanceReview;
import com.workforce.repository.PerformanceReviewRepository;

@Service
public class PerformanceReviewService {

	@Autowired
	private PerformanceReviewRepository performanceReviewRepository;

	public List<PerformanceReviewDTO> getTeamReviews(Long managerId) {
		List<PerformanceReview> reviews = performanceReviewRepository.findByEmployee_Manager_Id(managerId);

		return reviews.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public void submitManagerReview(Long reviewId, int rating, String feedback) {

		PerformanceReview review = performanceReviewRepository.findById(reviewId)
				.orElse(null);
		
		if (review == null) {
			return;
		}

		review.setManagerRating(rating);
		review.setManagerFeedback(feedback);
		review.setStatus("REVIEWED");

		performanceReviewRepository.save(review);
	}

	private PerformanceReviewDTO convertToDTO(PerformanceReview review) {

		PerformanceReviewDTO dto = new PerformanceReviewDTO();

		dto.setReviewId(review.getReviewId());
		dto.setEmployeeId(review.getEmployee().getId());

		dto.setEmployeeName(review.getEmployee().getFirstName() + " " + review.getEmployee().getLastName());

		dto.setSelfRating(review.getSelfRating());
		dto.setManagerRating(review.getManagerRating());
		dto.setManagerFeedback(review.getManagerFeedback());
		dto.setStatus(review.getStatus());

		return dto;
	}

}
