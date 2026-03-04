package com.revworkforce.employeecontroller;

import com.revworkforce.model.*;
import com.revworkforce.repository.*;

import jakarta.servlet.http.HttpSession;

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


    @GetMapping("/profile")
    public String profilePage(Model model) {
        model.addAttribute("employee", getEmployee());
        return "employee/profile";
    }


    // ================= OPEN EDIT PROFILE PAGE =================
    @GetMapping("/edit-profile")
    public String editProfilePage(Model model) {
        model.addAttribute("employee", getEmployee());
        return "employee/edit_profile";
    }


    // ================= UPDATE PROFILE =================
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute Employee employee,
                                @RequestParam(required = false) String oldPassword,
                                @RequestParam(required = false) String newPassword,
                                @RequestParam(required = false) String confirmPassword,
                                RedirectAttributes redirectAttributes) {

        Employee existing = getEmployee();

        // 🔹 Update basic details
        existing.setPhoneNumber(employee.getPhoneNumber());
        existing.setAddress(employee.getAddress());
        existing.setEmergencyContact(employee.getEmergencyContact());

        // ================= PASSWORD CHANGE =================
        if (oldPassword != null && !oldPassword.isEmpty()) {

            if (!passwordEncoder.matches(oldPassword, existing.getPassword())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Old password is incorrect!");
                return "redirect:/employee/edit-profile";
            }

            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Passwords do not match!");
                return "redirect:/employee/profile_edit";
            }

            existing.setPassword(passwordEncoder.encode(newPassword));
        }

        employeeRepo.save(existing);

        redirectAttributes.addFlashAttribute("successMessage",
                "Profile updated successfully!");

        return "redirect:/employee/profile";
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
    @PostMapping("/goals")
    public String submitGoal(@ModelAttribute Goal goal,
                             RedirectAttributes redirectAttributes) {

        Employee employee = getEmployee();
        goal.setEmployee(employee);
        goal.setProgress(0);

        goalRepository.save(goal);

        redirectAttributes.addFlashAttribute("successMessage",
                "Goal Added Successfully!");

        return "redirect:/employee/goals";
    }
    @PostMapping("/goals/update-progress/{id}")
    public String updateProgress(@PathVariable Long id,
                                 @RequestParam int progress,
                                 RedirectAttributes redirectAttributes) {

        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        goal.setProgress(progress);
        goalRepository.save(goal);

        redirectAttributes.addFlashAttribute("successMessage",
                "Progress Updated!");

        return "redirect:/employee/goals";
    }
    @GetMapping("/apply-leave")
    public String applyLeavePage(Model model) {
        model.addAttribute("employee", getEmployee());
        model.addAttribute("leaveTypes", leaveTypeRepo.findAll());
        return "employee/apply_leave";
    }

    @PostMapping("/apply-leave")
    public String submitLeave(@RequestParam Long leaveTypeId,
                              @RequestParam String startDate,
                              @RequestParam String endDate,
                              @RequestParam String reason,
                              RedirectAttributes redirectAttributes) {

        Employee employee = getEmployee();

        LeaveType leaveType = leaveTypeRepo.findById(leaveTypeId)
                .orElseThrow(() -> new RuntimeException("Leave type not found"));

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(leaveType);
        leaveRequest.setStartDate(LocalDate.parse(startDate));
        leaveRequest.setEndDate(LocalDate.parse(endDate));
        leaveRequest.setReason(reason);

        leaveRequestRepo.save(leaveRequest);

        redirectAttributes.addFlashAttribute("successMessage",
                "Leave Applied Successfully!");

        return "redirect:/employee/leave";
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
    @GetMapping("/directory")
    public String directoryPage(@RequestParam(required = false) String keyword,
                                Model model) {

        // ✅ ALWAYS use Spring Security method
        Employee employee = getEmployee();
        model.addAttribute("employee", employee);

        List<Employee> employees;

        if (keyword != null && !keyword.trim().isEmpty()) {
            employees = employeeRepo
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            keyword, keyword, keyword);
        } else {
            employees = employeeRepo.findAll();
        }

        model.addAttribute("employees", employees);
        model.addAttribute("keyword", keyword);

        return "employee/directory";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        Employee employee = getEmployee();

        // ================= GOALS =================
        List<Goal> goals = goalRepository.findByEmployee_Id(employee.getId());

        double goalPercentage = goals.stream()
                .mapToDouble(Goal::getProgress)
                .average()
                .orElse(0);

        // ================= LEAVE BALANCES =================
        List<LeaveBalance> balances =
                leaveBalanceRepo.findByEmployee_Id(employee.getId());

        int totalAllocated = balances.stream()
                .mapToInt(LeaveBalance::getTotalLeaves)
                .sum();

        int leavesUsed = balances.stream()
                .mapToInt(LeaveBalance::getUsedLeaves)
                .sum();

        int remainingLeaves = balances.stream()
                .mapToInt(LeaveBalance::getRemainingLeaves)
                .sum();

        int leaveUsagePercentage =
                totalAllocated > 0 ? (leavesUsed * 100) / totalAllocated : 0;

        // ================= PENDING REQUESTS =================
        List<LeaveRequest> requests =
                leaveRequestRepo.findByEmployee_Id(employee.getId());

        long pendingRequests = requests.stream()
                .filter(r -> r.getLeaveApproval() == null ||
                        (r.getLeaveApproval() != null &&
                         "Pending".equalsIgnoreCase(r.getLeaveApproval().getStatus())))
                .count();

        // ================= PERFORMANCE =================
        List<PerformanceReview> reviews =
                performanceReviewRepo.findByEmployee_Id(employee.getId());

        double performanceRating = reviews.stream()
                .filter(r -> r.getManagerRating() > 0)
                .mapToDouble(PerformanceReview::getManagerRating)
                .average()
                .orElse(0);

        // ================= MODEL =================
        model.addAttribute("employee", employee);
        model.addAttribute("goals", goals);
        model.addAttribute("goalPercentage", Math.round(goalPercentage));

        model.addAttribute("totalAllocated", totalAllocated);
        model.addAttribute("leavesUsed", leavesUsed);
        model.addAttribute("remainingLeaves", remainingLeaves);
        model.addAttribute("leaveUsagePercentage", leaveUsagePercentage);
        model.addAttribute("pendingRequests", pendingRequests);
        model.addAttribute("performanceRating", performanceRating);

        return "employee/employee-dashboard";
    }
}