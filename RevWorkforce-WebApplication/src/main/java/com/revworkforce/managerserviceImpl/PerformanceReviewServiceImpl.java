package com.revworkforce.managerserviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.dto.PerformanceReviewDTO;
import com.revworkforce.managerservice.PerformanceReviewService;
import com.revworkforce.model.PerformanceReview;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.PerformanceReviewRepository;

@Service
public class PerformanceReviewServiceImpl implements PerformanceReviewService {

    private final PerformanceReviewRepository reviewRepo;
    private final EmployeeRepository employeeRepo;
    private final NotificationService notificationService;

    public PerformanceReviewServiceImpl(
            PerformanceReviewRepository reviewRepo,
            EmployeeRepository employeeRepo,
            NotificationService notificationService) {

        this.reviewRepo = reviewRepo;
        this.employeeRepo = employeeRepo;
        this.notificationService = notificationService;
    }

    @Override
    public ApiResponse getTeamPerformanceReviews(Long managerId) {

        if (!employeeRepo.existsById(managerId)) {
            return new ApiResponse(404, "Manager not found", null);
        }

        List<PerformanceReview> reviews =
                reviewRepo.findByEmployee_Manager_Id(managerId);

        return new ApiResponse(200,
                "Team performance reviews fetched Successfully",
                reviews);
    }

    @Override
    public ApiResponse submitManagerFeedback(Long managerId,
                                             PerformanceReviewDTO dto) {

        PerformanceReview review =
                reviewRepo.findById(dto.getReviewId()).orElse(null);

        if (review == null) {
            return new ApiResponse(404,
                    "Performance review not found", null);
        }

        if (review.getEmployee().getManager() == null ||
            !review.getEmployee().getManager().getId().equals(managerId)) {

            return new ApiResponse(403,
                    "You are not authorized to review this employee",
                    null);
        }
        
        if ("REVIEWED".equals(review.getStatus())) {
            return new ApiResponse(400,
                    "Review already completed",
                    null);
        }

        if (dto.getManagerRating() == 0) {
            return new ApiResponse(400,
                    "Manager rating is required",
                    null);
        }

        if (dto.getManagerRating() < 1 ||
            dto.getManagerRating() > 5) {

            return new ApiResponse(400,
                    "Rating must be between 1 and 5",
                    null);
        }

        review.setManagerRating(dto.getManagerRating());
        review.setManagerFeedback(dto.getManagerFeedback());
        review.setStatus("REVIEWED");
        reviewRepo.save(review);

        NotificationDTO notification = new NotificationDTO();
        notification.setEmployeeId(review.getEmployee().getEmployeeId());
        notification.setTitle("Performance Reviewed");
        notification.setMessage("Your performance review has been evaluated by manager.");
        notification.setType("PERFORMANCE");
        notification.setStatus("REVIEWED");
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReferenceId(review.getId());

        notificationService.createNotification(notification);

        return new ApiResponse(200,
                "Performance review submitted successfully",
                review);
    }
    
}