package com.revworkforce.employeeservice;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.revworkforce.dto.PerformanceReviewDTO;
import com.revworkforce.model.Employee;
import com.revworkforce.model.PerformanceReview;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.PerformanceReviewRepository;

@Service
public class PerformanceService {

    private final PerformanceReviewRepository reviewRepository;
    private final EmployeeRepository employeeRepository;

    public PerformanceService(
            PerformanceReviewRepository reviewRepository,
            EmployeeRepository employeeRepository) {

        this.reviewRepository = reviewRepository;
        this.employeeRepository = employeeRepository;
    }

    // CREATE REVIEW
    public PerformanceReviewDTO createReview(
            Long employeeId,
            PerformanceReviewDTO dto) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        if (dto.getSelfRating() < 1 ||
            dto.getSelfRating() > 5) {

            throw new RuntimeException(
                    "Self rating must be between 1 and 5");
        }

        PerformanceReview review = new PerformanceReview();

        review.setEmployee(employee);
        review.setDeliverables(dto.getDeliverables());
        review.setAccomplishments(dto.getAccomplishments());
        review.setAreasOfImprovement(dto.getAreasOfImprovement());
        review.setSelfRating(dto.getSelfRating());
        review.setStatus("Draft");

        PerformanceReview saved =
                reviewRepository.save(review);

        return mapToDTO(saved);
    }

    // SUBMIT REVIEW
    public PerformanceReviewDTO submitReview(Long reviewId) {

        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new RuntimeException("Review not found"));

        // Already submitted check
        if ("SUBMITTED".equalsIgnoreCase(review.getStatus())) {
            throw new IllegalStateException(
                    "Performance review is already submitted.");
        }

        // Only draft allowed
        if (!"DRAFT".equalsIgnoreCase(review.getStatus())) {
            throw new IllegalStateException(
                    "Only Draft reviews can be submitted.");
        }

        review.setStatus("SUBMITTED");
        review.setSubmittedDate(LocalDate.now());

        return mapToDTO(reviewRepository.save(review));
    }

    // GET EMPLOYEE REVIEWS
    public List<PerformanceReviewDTO>
        getEmployeeReviews(Long employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        return reviewRepository
                .findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // VIEW FEEDBACK
    public PerformanceReviewDTO viewFeedback(Long reviewId) {

        PerformanceReview review =
                reviewRepository.findById(reviewId)
                        .orElseThrow(() ->
                                new RuntimeException("Review not found"));

        return mapToDTO(review);
    }

    private PerformanceReviewDTO
        mapToDTO(PerformanceReview review) {

        PerformanceReviewDTO dto =
                new PerformanceReviewDTO();

        dto.setReviewId(review.getId());
        dto.setDeliverables(review.getDeliverables());
        dto.setAccomplishments(review.getAccomplishments());
        dto.setAreasOfImprovement(review.getAreasOfImprovement());
        dto.setSelfRating(review.getSelfRating());
        dto.setStatus(review.getStatus());
        dto.setManagerFeedback(review.getManagerFeedback());

        return dto;
    }
}
