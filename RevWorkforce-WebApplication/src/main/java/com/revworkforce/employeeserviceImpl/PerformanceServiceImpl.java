package com.revworkforce.employeeserviceImpl;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.PerformanceReviewDTO;
import com.revworkforce.employeeservice.PerformanceService;
import com.revworkforce.exception.EmployeeNotFoundException;
import com.revworkforce.model.Employee;
import com.revworkforce.model.PerformanceReview;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.PerformanceReviewRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerformanceServiceImpl implements PerformanceService {

    private final PerformanceReviewRepository reviewRepo;
    private final EmployeeRepository employeeRepo;

    public PerformanceServiceImpl(PerformanceReviewRepository reviewRepo,
                                  EmployeeRepository employeeRepo) {
        this.reviewRepo = reviewRepo;
        this.employeeRepo = employeeRepo;
    }

    // ================= CREATE SELF REVIEW =================
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

        return new ApiResponse(
                201,
                "Self review created successfully",
                mapToDTO(saved)
        );
    }

    // ================= GET EMPLOYEE REVIEWS =================
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

    // ================= SUBMIT REVIEW =================
    @Override
    public ApiResponse submitReview(Long reviewId) {

        PerformanceReview review = reviewRepo.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        review.setStatus("Submitted");
        review.setSubmittedDate(LocalDate.now());

        reviewRepo.save(review);

        return new ApiResponse(
                200,
                "Review submitted successfully",
                null
        );
    }

    // ================= DELETE REVIEW =================
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

        return new ApiResponse(
                200,
                "Review deleted successfully",
                null
        );
    }

    // ================= DTO MAPPING =================
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