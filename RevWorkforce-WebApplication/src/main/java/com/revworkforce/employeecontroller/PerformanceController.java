package com.revworkforce.employeecontroller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.revworkforce.dto.PerformanceReviewDTO;
import com.revworkforce.employeeservice.PerformanceService;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;

@Controller
@RequestMapping("/employee/performance")
public class PerformanceController {

    private final PerformanceService performanceService;
    private final EmployeeRepository employeeRepository;

    public PerformanceController(PerformanceService performanceService,
                                 EmployeeRepository employeeRepository) {
        this.performanceService = performanceService;
        this.employeeRepository = employeeRepository;
    }

    // ================== LOAD PAGE ==================
    @GetMapping
    public String showPerformancePage(Model model, Principal principal) {

        Employee employee = employeeRepository
                .findByEmail(principal.getName())
                .orElseThrow();

        model.addAttribute("employee", employee);
        model.addAttribute("review", new PerformanceReviewDTO());
        model.addAttribute("reviews",
                performanceService.getEmployeeReviews(employee.getId()));
        model.addAttribute("showFeedback", false);

        return "employee/performance";
    }

    // ================== SAVE REVIEW (DRAFT) ==================
    @PostMapping
    public String createReview(@ModelAttribute("review") PerformanceReviewDTO dto,
                               Principal principal) {

        Employee employee = employeeRepository
                .findByEmail(principal.getName())
                .orElseThrow();

        performanceService.createReview(employee.getId(), dto);

        return "redirect:/employee/performance";
    }

    // ================== SUBMIT REVIEW ==================
    @GetMapping("/submit/{reviewId}")
    public String submitReview(@PathVariable Long reviewId,
                               RedirectAttributes redirectAttributes) {

        try {
            performanceService.submitReview(reviewId);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Performance review submitted to manager successfully");

        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Already submitted");
        }

        return "redirect:/employee/performance";
    }

    // ================== VIEW FEEDBACK ==================
    @GetMapping("/feedback/{reviewId}")
    public String viewFeedback(@PathVariable Long reviewId,
                               Model model,
                               Principal principal) {

        Employee employee = employeeRepository
                .findByEmail(principal.getName())
                .orElseThrow();

        model.addAttribute("employee", employee);
        model.addAttribute("review", new PerformanceReviewDTO());
        model.addAttribute("reviews",
                performanceService.getEmployeeReviews(employee.getId()));

        model.addAttribute("selectedFeedback",
                performanceService.viewFeedback(reviewId));
        model.addAttribute("showFeedback", true);

        return "employee/performance";
    }
}