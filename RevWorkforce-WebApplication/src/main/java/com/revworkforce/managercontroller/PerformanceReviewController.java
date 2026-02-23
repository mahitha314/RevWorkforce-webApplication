package com.revworkforce.managercontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.workforce.dto.PerformanceReviewDTO;
import com.workforce.managerservice.PerformanceReviewService;

//@RestController
//@RequestMapping("/api/manager/performance-reviews")
//public class PerformanceReviewController {
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
	
//	@GetMapping
//	public List<PerformanceReviewDTO> viewReviews() {
//	    Long managerId = 1L;
//	    return performanceReviewService.getTeamReviews(managerId);
//	}

	@PostMapping("/submit")
	public String submitReview(@RequestParam Long reviewId, @RequestParam int rating, @RequestParam String feedback) {
		performanceReviewService.submitManagerReview(reviewId, rating, feedback);
		return "redirect:/manager/performance-reviews";
	}
}