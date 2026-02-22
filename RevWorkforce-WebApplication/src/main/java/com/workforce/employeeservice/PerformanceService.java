package com.workforce.employeeservice;

import com.workforce.dto.PerformanceReviewDTO;
import com.workforce.model.Employee;
import com.workforce.model.PerformanceReview;
import com.workforce.repository.EmployeeRepository;
import com.workforce.repository.PerformanceReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerformanceService {

    private final PerformanceReviewRepository reviewRepository;
    private final EmployeeRepository employeeRepository;

    public PerformanceService(PerformanceReviewRepository reviewRepository,
                              EmployeeRepository employeeRepository) {
        this.reviewRepository = reviewRepository;
        this.employeeRepository = employeeRepository;
    }

    // ✅ Create Draft Review
    public PerformanceReviewDTO createReview(Long employeeId, PerformanceReviewDTO dto) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (dto.getSelfRating() < 1 || dto.getSelfRating() > 5) {
            throw new RuntimeException("Self rating must be between 1 and 5");
        }

        PerformanceReview review = new PerformanceReview();
        review.setEmployee(employee);
        review.setDeliverables(dto.getDeliverables());
        review.setAccomplishments(dto.getAccomplishments());
        review.setAreasOfImprovement(dto.getAreasOfImprovement());
        review.setSelfRating(dto.getSelfRating());
        review.setStatus("Draft");

        PerformanceReview saved = reviewRepository.save(review);

        return mapToDTO(saved);
    }

    // ✅ Submit Review
    public PerformanceReviewDTO submitReview(Long reviewId) {

        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!"Draft".equals(review.getStatus())) {
            throw new RuntimeException("Only Draft reviews can be submitted");
        }

        review.setStatus("Submitted");
        review.setSubmittedDate(LocalDate.now());

        PerformanceReview updated = reviewRepository.save(review);

        return mapToDTO(updated);
    }

    // ✅ Get Employee Reviews
    public List<PerformanceReviewDTO> getEmployeeReviews(Long employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return reviewRepository.findByEmployeeEmployeeId(employeeId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ✅ View Feedback
    public PerformanceReviewDTO viewFeedback(Long reviewId) {

        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        return mapToDTO(review);
    }

    // 🔹 Common Mapper Method
    private PerformanceReviewDTO mapToDTO(PerformanceReview review) {

        PerformanceReviewDTO dto = new PerformanceReviewDTO();
        dto.setReviewId(review.getReviewId());
        dto.setDeliverables(review.getDeliverables());
        dto.setAccomplishments(review.getAccomplishments());
        dto.setAreasOfImprovement(review.getAreasOfImprovement());
        dto.setSelfRating(review.getSelfRating());
        dto.setStatus(review.getStatus());
        dto.setManagerFeedback(review.getManagerFeedback());

        return dto;
    }
}