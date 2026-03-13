package com.revworkforce.managerserviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.dto.PerformanceReviewDTO;
import com.revworkforce.managerservice.PerformanceReviewService;
import com.revworkforce.model.Employee;
import com.revworkforce.model.PerformanceReview;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.PerformanceReviewRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class PerformanceReviewServiceImpl implements PerformanceReviewService {

	private static final Logger logger = LogManager.getLogger(PerformanceReviewServiceImpl.class);

	private final PerformanceReviewRepository reviewRepo;
	private final EmployeeRepository employeeRepo;
	private final NotificationService notificationService;
	private final ActivityLogService activityLogService;

	public PerformanceReviewServiceImpl(PerformanceReviewRepository reviewRepo, EmployeeRepository employeeRepo,
			NotificationService notificationService, ActivityLogService activityLogService) {

		this.reviewRepo = reviewRepo;
		this.employeeRepo = employeeRepo;
		this.notificationService = notificationService;
		this.activityLogService = activityLogService;

	}

	@Override
	public ApiResponse getTeamPerformanceReviews(Long managerId) {

		logger.info("Fetching team performance reviews for managerId: {}", managerId);

		if (!employeeRepo.existsById(managerId)) {

			logger.warn("Manager not found with id: {}", managerId);
			return new ApiResponse(404, "Manager not found", null);
		}

		List<PerformanceReviewDTO> reviews = reviewRepo.findByEmployee_Manager_Id(managerId).stream()
				.map(this::mapToDTO).collect(Collectors.toList());

		logger.debug("Total reviews fetched for manager {} : {}", managerId, reviews.size());

		activityLogService.log(managerId, "Viewed team performance reviews");

		logger.info("Team performance reviews fetched successfully");

		return new ApiResponse(200, "Team performance reviews fetched", reviews);
	}

	@Override
	public ApiResponse submitManagerFeedback(Long managerId, PerformanceReviewDTO dto) {

		logger.info("Manager {} submitting feedback for reviewId {}", managerId, dto.getReviewId());

		PerformanceReview review = reviewRepo.findById(dto.getReviewId()).orElse(null);

		if (review == null) {

			logger.error("Performance review not found for reviewId {}", dto.getReviewId());
			return new ApiResponse(404, "Performance review not found", null);
		}

		if (review.getEmployee().getManager() == null || !review.getEmployee().getManager().getId().equals(managerId)) {

			logger.warn("Manager {} tried to review unauthorized employee", managerId);
			return new ApiResponse(403, "You are not authorized to review this employee", null);
		}

		if ("REVIEWED".equals(review.getStatus())) {

			logger.warn("Review already completed for reviewId {}", review.getId());
			return new ApiResponse(400, "Review already completed", null);
		}

		if (!"Submitted".equalsIgnoreCase(review.getStatus())) {

			logger.warn("Employee has not submitted the review yet for reviewId {}", review.getId());
			return new ApiResponse(400, "Employee has not submitted the review yet", null);
		}

		if (dto.getManagerRating() == 0) {

			logger.warn("Manager rating missing for reviewId {}", review.getId());
			return new ApiResponse(400, "Manager rating is required", null);
		}

		if (dto.getManagerRating() < 1 || dto.getManagerRating() > 5) {

			logger.warn("Invalid rating {} given for reviewId {}", dto.getManagerRating(), review.getId());
			return new ApiResponse(400, "Rating must be between 1 and 5", null);
		}

		logger.debug("Updating manager feedback for reviewId {}", review.getId());

		review.setManagerRating(dto.getManagerRating());
		review.setManagerFeedback(dto.getManagerFeedback());
		review.setStatus("REVIEWED");
		reviewRepo.save(review);

		logger.info("Performance review updated successfully for reviewId {}", review.getId());

		Employee employee = review.getEmployee();

		NotificationDTO notification = new NotificationDTO();
		notification.setEmployeeId(review.getEmployee().getEmployeeId());
		notification.setTitle("Performance Reviewed");
		notification.setMessage("Your performance review has been evaluated by manager.");
		notification.setType("PERFORMANCE");
		notification.setStatus("DELIVERED");
		notification.setIsRead(false);
		notification.setCreatedAt(LocalDateTime.now());
		notification.setReferenceId(review.getId());

		notificationService.createNotification(notification);

		logger.info("Notification sent to employeeId {}", employee.getEmployeeId());

		activityLogService.log(managerId, "Reviewed performance of employee ID: " + employee.getId());

		logger.info("Manager {} successfully reviewed employee {}", managerId, employee.getId());

		return new ApiResponse(200, "Performance review submitted successfully", mapToDTO(review));
	}

	private PerformanceReviewDTO mapToDTO(PerformanceReview review) {

		return new PerformanceReviewDTO(review.getId(), review.getEmployee().getId(),
				review.getEmployee().getFirstName() + " " + review.getEmployee().getLastName(),
				review.getAccomplishments(), review.getDeliverables(), review.getAreasOfImprovement(),
				review.getSelfRating(), review.getManagerRating(), review.getManagerFeedback(), review.getStatus(),
				review.getSubmittedDate());
	}

}