package com.revworkforce.employeecontroller;

import com.revworkforce.model.*;
import com.revworkforce.repository.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeePageController {

    private final EmployeeRepository employeeRepo;
    private final LeaveBalanceRepository leaveBalanceRepo;
    private final LeaveTypeRepository leaveTypeRepo;
    private final LeaveRequestRepository leaveRequestRepo;
    private final GoalRepository goalRepository;
    private final AnnouncementRepository announcementRepo;
    private final NotificationRepository notificationRepo;
    private final PerformanceReviewRepository performanceReviewRepo;
    private final HolidayRepository holidayRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeePageController(EmployeeRepository employeeRepo,
                                  LeaveBalanceRepository leaveBalanceRepo,
                                  LeaveTypeRepository leaveTypeRepo,
                                  LeaveRequestRepository leaveRequestRepo,
                                  GoalRepository goalRepository,
                                  AnnouncementRepository announcementRepo,
                                  NotificationRepository notificationRepo,
                                  PerformanceReviewRepository performanceReviewRepo,
                                  HolidayRepository holidayRepository,
                                  PasswordEncoder passwordEncoder) {

        this.employeeRepo = employeeRepo;
        this.leaveBalanceRepo = leaveBalanceRepo;
        this.leaveTypeRepo = leaveTypeRepo;
        this.leaveRequestRepo = leaveRequestRepo;
        this.goalRepository = goalRepository;
        this.announcementRepo = announcementRepo;
        this.notificationRepo = notificationRepo;
        this.performanceReviewRepo = performanceReviewRepo;
        this.holidayRepository = holidayRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= GET LOGGED EMPLOYEE =================
    private Employee getEmployee() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return employeeRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // ================= PROFILE =================
    @GetMapping("/profile")
    public String profilePage(Model model) {
        model.addAttribute("employee", getEmployee());
        return "employee/profile";
    }

    // ================= UPDATE PROFILE =================
    @PostMapping("/profile/update-all")
    public String updateAllDetails(@ModelAttribute Employee employee,
                                   @RequestParam(required = false) String oldPassword,
                                   @RequestParam(required = false) String newPassword,
                                   @RequestParam(required = false) String confirmPassword,
                                   RedirectAttributes redirectAttributes) {

        Employee existing = getEmployee();

        existing.setPhoneNumber(employee.getPhoneNumber());
        existing.setAddress(employee.getAddress());
        existing.setEmergencyContact(employee.getEmergencyContact());

        if (oldPassword != null && !oldPassword.isEmpty()) {

            if (!passwordEncoder.matches(oldPassword, existing.getPassword())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Old password incorrect!");
                return "redirect:/employee/profile";
            }

            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Passwords do not match!");
                return "redirect:/employee/profile";
            }

            if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Password must be 8+ chars with uppercase, lowercase & number.");
                return "redirect:/employee/profile";
            }

            existing.setPassword(passwordEncoder.encode(newPassword));
        }

        employeeRepo.save(existing);

        redirectAttributes.addFlashAttribute("successMessage",
                "Profile updated successfully!");

        return "redirect:/employee/profile";
    }

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        Employee employee = getEmployee();

        model.addAttribute("employee", employee);
        model.addAttribute("goals",
                goalRepository.findByEmployee_Id(employee.getId()));

        return "employee/dashboard";
    }

    // ================= LEAVE =================
    @GetMapping("/leave")
    public String leavePage(Model model) {

        Employee employee = getEmployee();

        model.addAttribute("employee", employee);
        model.addAttribute("leaveBalances",
                leaveBalanceRepo.findByEmployee_Id(employee.getId()));
        model.addAttribute("leaveTypes", leaveTypeRepo.findAll());
        model.addAttribute("leaves",
                leaveRequestRepo.findByEmployee_Id(employee.getId()));

        return "employee/my_leaves";
    }

    // ================= GOALS =================
    @GetMapping("/goals")
    public String goalsPage(Model model) {

        Employee employee = getEmployee();

        model.addAttribute("goal", new Goal());
        model.addAttribute("goals",
                goalRepository.findByEmployee_Id(employee.getId()));

        return "employee/goals";
    }

    // ================= PERFORMANCE =================
    @GetMapping("/performance")
    public String performancePage(Model model) {

        Employee employee = getEmployee();

        List<PerformanceReview> reviews =
                performanceReviewRepo.findByEmployee_Id(employee.getId());

        model.addAttribute("reviews", reviews);
        model.addAttribute("review", new PerformanceReview());

        return "employee/performance";
    }

    @PostMapping("/performance")
    public String saveReview(@ModelAttribute PerformanceReview review,
                             RedirectAttributes redirectAttributes) {

        Employee employee = getEmployee();

        review.setEmployee(employee);
        review.setStatus("Draft");

        performanceReviewRepo.save(review);

        redirectAttributes.addFlashAttribute("successMessage",
                "Performance Review Saved!");

        return "redirect:/employee/performance";
    }

    @GetMapping("/performance/submit/{id}")
    public String submitReview(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {

        PerformanceReview review = performanceReviewRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setStatus("Submitted");
        performanceReviewRepo.save(review);

        redirectAttributes.addFlashAttribute("successMessage",
                "Review Submitted Successfully!");

        return "redirect:/employee/performance";
    }

    // ================= ANNOUNCEMENTS =================
    @GetMapping("/announcements")
    public String announcementsPage(Model model) {

        model.addAttribute("announcements",
                announcementRepo.findAll());

        return "employee/announcements";
    }

    // ================= NOTIFICATIONS =================
    @GetMapping("/notifications")
    public String notificationsPage(Model model) {

        Employee employee = getEmployee();

        model.addAttribute("notifications",
                notificationRepo
                        .findByEmployee_IdOrderByCreatedAtDesc(employee.getId()));

        return "employee/notifications";
    }

    // ================= HOLIDAYS =================
    @GetMapping("/holidays")
    public String holidaysPage(Model model) {

        model.addAttribute("holidays",
                holidayRepository
                        .findByHolidayDateGreaterThanEqualOrderByHolidayDateAsc(LocalDate.now()));

        return "employee/holidays";
    }
}