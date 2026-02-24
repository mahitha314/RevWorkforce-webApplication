package com.revworkforce.employeecontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.revworkforce.dto.PerformanceReviewDTO;
import com.revworkforce.employeeservice.PerformanceService;

@Controller
@RequestMapping("/employee/performance")
public class PerformanceController {

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    
    @GetMapping("/{employeeId}")
    public String showPerformancePage(@PathVariable Long employeeId, Model model) {

        model.addAttribute("employeeId", employeeId);
        model.addAttribute("review", new PerformanceReviewDTO());
        model.addAttribute("reviews",
                performanceService.getEmployeeReviews(employeeId));

        return "employee/performance";  
    }

   
    @PostMapping("/{employeeId}")
    public String createReview(@PathVariable Long employeeId,
                               @ModelAttribute("review") PerformanceReviewDTO dto) {

        performanceService.createReview(employeeId, dto);

        return "redirect:/employee/performance/" + employeeId;
    }

    
    @GetMapping("/{employeeId}/submit/{reviewId}")
    public String submitReview(@PathVariable Long employeeId,
                               @PathVariable Long reviewId) {

        performanceService.submitReview(reviewId);

        return "redirect:/employee/performance/" + employeeId;
    }

    
    @GetMapping("/{employeeId}/feedback/{reviewId}")
    public String viewFeedback(@PathVariable Long employeeId,
                               @PathVariable Long reviewId,
                               Model model) {

        model.addAttribute("feedback",
                performanceService.viewFeedback(reviewId));

        return "employee/feedback"; 
    }
}