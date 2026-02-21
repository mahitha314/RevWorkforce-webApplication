package com.workforce.managercontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.workforce.managerservice.PerformanceReviewService;

@Controller
@RequestMapping("/manager/performance-reviews")
public class PerformanceReviewController {

	@Autowired
	private PerformanceReviewService performanceReviewService;

	@GetMapping
	public String viewReviews(Model model) {
		Long managerId = 1L;
		model.addAttribute("reviews", performanceReviewService.getTeamReviews(managerId));
		return "manager/performance_review";
	}

	@PostMapping("/submit")
	public String submitReview(@RequestParam Long reviewId, @RequestParam int rating, @RequestParam String feedback) {
		performanceReviewService.submitManagerReview(reviewId, rating, feedback);
		return "redirect:/manager/performance-reviews";
	}
}