package com.revworkforce.managerserviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.PerformanceReviewDTO;
import com.revworkforce.managerservice.PerformanceReviewService;
import com.revworkforce.model.Notification;
import com.revworkforce.model.PerformanceReview;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.NotificationRepository;
import com.revworkforce.repository.PerformanceReviewRepository;

@Service
public class PerformanceReviewServiceImpl implements PerformanceReviewService {

	private final PerformanceReviewRepository reviewRepo;
	private final EmployeeRepository employeeRepo;
	private final NotificationRepository notificationRepo;

	public PerformanceReviewServiceImpl(PerformanceReviewRepository reviewRepo, EmployeeRepository employeeRepo,
			NotificationRepository notificationRepo) {

		this.reviewRepo = reviewRepo;
		this.employeeRepo = employeeRepo;
		this.notificationRepo = notificationRepo;
	}

	@Override
	public ApiResponse getTeamPerformanceReviews(Long managerId) {

		if (!employeeRepo.existsById(managerId)) {
			return new ApiResponse(404, "Manager not found", null);
		}

		List<PerformanceReview> reviews = reviewRepo.findByEmployee_Manager_Id(managerId);

		List<PerformanceReviewDTO> reviewDTOs = reviews.stream()
				.map(r -> new PerformanceReviewDTO(r.getId(), r.getEmployee().getId(),
						r.getEmployee().getFirstName() + " " + r.getEmployee().getLastName(), r.getAccomplishments(),
						r.getDeliverables(), r.getAreasOfImprovement(), r.getSelfRating(), r.getManagerRating(),
						r.getManagerFeedback(), r.getStatus(), r.getSubmittedDate()))
				.toList();

		return new ApiResponse(200, "Team performance reviews fetched", reviewDTOs);
	}

	@Override
	public ApiResponse submitManagerFeedback(Long managerId, PerformanceReviewDTO dto) {

		PerformanceReview review = reviewRepo.findById(dto.getReviewId()).orElse(null);

		if (review == null) {
			return new ApiResponse(404, "Performance review not found", null);
		}

		if (review.getEmployee().getManager() == null || !review.getEmployee().getManager().getId().equals(managerId)) {

			return new ApiResponse(403, "You are not authorized to review this employee", null);
		}

		if ("REVIEWED".equals(review.getStatus())) {
			return new ApiResponse(400, "Review already completed", null);
		}

		if (dto.getManagerRating() == 0) {
			return new ApiResponse(400, "Manager rating is required", null);
		}

		if (dto.getManagerRating() < 1 || dto.getManagerRating() > 5) {

			return new ApiResponse(400, "Rating must be between 1 and 5", null);
		}

		review.setManagerRating(dto.getManagerRating());
		review.setManagerFeedback(dto.getManagerFeedback());
		review.setStatus("REVIEWED");
		reviewRepo.save(review);

		Notification notification = new Notification();
		notification.setEmployee(review.getEmployee());
		notification.setTitle("Performance Reviewed");
		notification.setMessage("Your performance review has been evaluated.");
		notification.setStatus("ACTIVE");
		notification.setType("PERFORMANCE");
		notification.setIsRead(false);
		notification.setCreatedAt(LocalDateTime.now());
		notificationRepo.save(notification);

		return new ApiResponse(200, "Performance review submitted successfully", review);
	}

}