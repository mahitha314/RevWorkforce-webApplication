package com.revworkforce.employeeserviceImpl;

import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.dto.PerformanceReviewDTO;
import com.revworkforce.employeeservice.PerformanceService;
import com.revworkforce.exception.EmployeeNotFoundException;
import com.revworkforce.model.Employee;
import com.revworkforce.model.PerformanceReview;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.PerformanceReviewRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PerformanceServiceImpl implements PerformanceService {

	private final PerformanceReviewRepository reviewRepo;
    private final EmployeeRepository employeeRepo;
    private final NotificationService notificationService;
    private final ActivityLogService activityLogService;

    public PerformanceServiceImpl(PerformanceReviewRepository reviewRepo,
                                  EmployeeRepository employeeRepo,
                                  NotificationService notificationService,
                                  ActivityLogService activityLogService) {
        this.reviewRepo = reviewRepo;
        this.employeeRepo = employeeRepo;
        this.notificationService = notificationService;
        this.activityLogService = activityLogService;
    }

   
    @Override
    public ApiResponse createSelfReview(Long employeeId, PerformanceReviewDTO dto) {

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found"));

        PerformanceReview review = new PerformanceReview();
        review.setEmployee(employee);
        review.setAccomplishments(dto.getAccomplishments());
        review.setDeliverables(dto.getDeliverables());
        review.setAreasOfImprovement(dto.getAreasOfImprovement());
        review.setSelfRating(dto.getSelfRating());
        review.setManagerRating(0);
        review.setManagerFeedback(null);
        review.setStatus("Draft");
        review.setSubmittedDate(LocalDate.now());

        PerformanceReview saved = reviewRepo.save(review);
        
     // Activity Log
        activityLogService.log(
                employee.getId(),
                "Created self performance review draft"
        );

        return new ApiResponse(
                201,
                "Self review created successfully",
                mapToDTO(saved)
        );
    }

    @Override
    public ApiResponse getEmployeeReviews(Long employeeId) {

        employeeRepo.findById(employeeId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found"));

        List<PerformanceReviewDTO> reviews =
                reviewRepo.findByEmployee_Id(employeeId)
                        .stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());

        return new ApiResponse(
                200,
                "Performance reviews fetched successfully",
                reviews
        );
    }

  
    @Override
    public ApiResponse submitReview(Long reviewId) {

        PerformanceReview review = reviewRepo.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        review.setStatus("Submitted");
        review.setSubmittedDate(LocalDate.now());

        reviewRepo.save(review);
        
        Employee employee = review.getEmployee();

       
        if (employee.getManager() != null) {

            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setTitle("Performance Review Submitted");
            notificationDTO.setMessage(
                    employee.getFirstName() + " has submitted performance review"
            );
            notificationDTO.setReferenceId(employee.getManager().getId());

            notificationService.createNotification(notificationDTO);
        }

        // Activity Log
        activityLogService.log(
                employee.getId(),
                "Submitted performance review"
        );

        return new ApiResponse(
                200,
                "Review submitted successfully",
                null
        );
    }

    
    @Override
    public ApiResponse deleteReview(Long reviewId) {

        PerformanceReview review = reviewRepo.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        if ("Submitted".equalsIgnoreCase(review.getStatus())) {
            return new ApiResponse(
                    400,
                    "Submitted review cannot be deleted",
                    null
            );
        }

        reviewRepo.delete(review);
        
     // Activity Log
        activityLogService.log(
                review.getEmployee().getId(),
                "Deleted performance review draft"
        );

        return new ApiResponse(
                200,
                "Review deleted successfully",
                null
        );
    }

    private PerformanceReviewDTO mapToDTO(PerformanceReview review) {

        return new PerformanceReviewDTO(
                review.getId(),
                review.getEmployee().getId(),
                review.getEmployee().getFirstName() + " " +
                        review.getEmployee().getLastName(),
                review.getAccomplishments(),
                review.getDeliverables(),
                review.getAreasOfImprovement(),
                review.getSelfRating(),
                review.getManagerRating(),
                review.getManagerFeedback(),
                review.getStatus(),
                review.getSubmittedDate()
        );
    }
}