package com.revworkforce.employeecontroller;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.PerformanceReviewDTO;
import com.revworkforce.employeeservice.PerformanceService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/performance")
public class PerformanceController {

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @PostMapping("/{employeeId}")
    public ApiResponse createReview(@PathVariable Long employeeId,
                                    @RequestBody PerformanceReviewDTO dto) {
        return performanceService.createSelfReview(employeeId, dto);
    }

    @GetMapping("/{employeeId}")
    public ApiResponse getReviews(@PathVariable Long employeeId) {
        return performanceService.getEmployeeReviews(employeeId);
    }

    @PutMapping("/submit/{reviewId}")
    public ApiResponse submitReview(@PathVariable Long reviewId) {
        return performanceService.submitReview(reviewId);
    }

    @DeleteMapping("/{reviewId}")
    public ApiResponse deleteReview(@PathVariable Long reviewId) {
        return performanceService.deleteReview(reviewId);
    }
}