package com.revworkforce.employeecontroller;

import com.revworkforce.model.*;
import com.revworkforce.repository.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

    public EmployeePageController(EmployeeRepository employeeRepo,
                                  LeaveBalanceRepository leaveBalanceRepo,
                                  LeaveTypeRepository leaveTypeRepo,
                                  LeaveRequestRepository leaveRequestRepo,
                                  GoalRepository goalRepository,
                                  AnnouncementRepository announcementRepo,
                                  NotificationRepository notificationRepo,
                                  PerformanceReviewRepository performanceReviewRepo,
                                  HolidayRepository holidayRepository) {

        this.employeeRepo = employeeRepo;
        this.leaveBalanceRepo = leaveBalanceRepo;
        this.leaveTypeRepo = leaveTypeRepo;
        this.leaveRequestRepo = leaveRequestRepo;
        this.goalRepository = goalRepository;
        this.announcementRepo = announcementRepo;
        this.notificationRepo = notificationRepo;
        this.performanceReviewRepo = performanceReviewRepo;
        this.holidayRepository = holidayRepository;
    }

    // ================= GET LOGGED EMPLOYEE =================
    private Employee getEmployee() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return employeeRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        Employee employee = getEmployee();

        List<LeaveBalance> balances =
                leaveBalanceRepo.findByEmployee_Id(employee.getId());

        List<LeaveRequest> leaveRequests =
                leaveRequestRepo.findByEmployee_Id(employee.getId());

        List<Goal> goals =
                goalRepository.findByEmployee_Id(employee.getId());

        List<PerformanceReview> reviews =
                performanceReviewRepo.findByEmployee_Id(employee.getId());

        int totalAllocated = 0;
        int totalUsed = 0;
        int totalRemaining = 0;
        int pendingCount = 0;

        for (LeaveBalance b : balances) {
            totalAllocated += b.getTotalLeaves();
            totalUsed += b.getUsedLeaves();
            totalRemaining += b.getRemainingLeaves();
        }

        // Pending = Not yet approved
        for (LeaveRequest r : leaveRequests) {
            if (r.getLeaveApproval() == null) {
                pendingCount++;
            }
        }

        int totalProgress = goals.stream()
                .mapToInt(Goal::getProgress)
                .sum();

        int goalPercentage =
                goals.isEmpty() ? 0 : totalProgress / goals.size();

        double finalRating = reviews.stream()
                .mapToDouble(r -> {
                    double manager = r.getManagerRating();
                    double self = r.getSelfRating();
                    return manager == 0 ? self : (manager * 0.6) + (self * 0.4);
                })
                .average()
                .orElse(0);

        model.addAttribute("employee", employee);
        model.addAttribute("totalAllocated", totalAllocated);
        model.addAttribute("totalUsed", totalUsed);
        model.addAttribute("totalRemaining", totalRemaining);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("goalPercentage", goalPercentage);
        model.addAttribute("avgRating", finalRating);
        model.addAttribute("goals", goals);

        return "employee/dashboard";
    }

    // ================= PROFILE PAGE =================
    @GetMapping("/profile")
    public String profilePage(Model model) {

        Employee employee = getEmployee();
        model.addAttribute("employee", employee);

        return "employee/profile";
    }

    // ================= LEAVE PAGE =================
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

    // ================= APPLY LEAVE =================
    @PostMapping("/apply-leave")
    public String applyLeave(@RequestParam Long leaveTypeId,
                             @RequestParam String startDate,
                             @RequestParam String endDate,
                             @RequestParam String reason,
                             RedirectAttributes redirectAttributes) {

        Employee employee = getEmployee();

        LeaveType leaveType = leaveTypeRepo.findById(leaveTypeId)
                .orElseThrow(() -> new RuntimeException("Leave type not found"));

        LeaveRequest request = new LeaveRequest();
        request.setEmployee(employee);
        request.setLeaveType(leaveType);
        request.setStartDate(LocalDate.parse(startDate));
        request.setEndDate(LocalDate.parse(endDate));
        request.setReason(reason);

        leaveRequestRepo.save(request);

        redirectAttributes.addFlashAttribute("successMessage",
                "Leave Applied Successfully!");

        return "redirect:/employee/leave";
    }

    // ================= CANCEL LEAVE =================
    @PostMapping("/cancel/{id}")
    public String cancelLeave(@PathVariable Long id,
                              RedirectAttributes redirectAttributes) {

        LeaveRequest leave = leaveRequestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        if (leave.getLeaveApproval() == null) {
            leaveRequestRepo.delete(leave);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Leave Cancelled Successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Cannot cancel approved/rejected leave");
        }

        return "redirect:/employee/leave";
    }

 // ================= EMPLOYEE DIRECTORY =================
    @GetMapping("/directory")
    public String employeeDirectory(
            @RequestParam(required = false) String keyword,
            Model model) {

        List<Employee> employees;

        if (keyword != null && !keyword.isEmpty()) {
            employees = employeeRepo
                    .findByFirstNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            keyword, keyword);
        } else {
            employees = employeeRepo.findAll();
        }

        model.addAttribute("employees", employees);
        model.addAttribute("keyword", keyword);

        return "employee/directory";
    }
 // ================= GOALS PAGE =================
    @GetMapping("/goals")
    public String goalsPage(Model model) {

        Employee employee = getEmployee();

        List<Goal> goals =
                goalRepository.findByEmployee_Id(employee.getId());

        model.addAttribute("goal", new Goal()); // for form binding
        model.addAttribute("goals", goals);

        return "employee/goals";
    }
 // ================= ADD GOAL =================
    @PostMapping("/goals")
    public String addGoal(@ModelAttribute Goal goal,
                          RedirectAttributes redirectAttributes) {

        Employee employee = getEmployee();

        goal.setEmployee(employee);
        goal.setProgress(0);

        // 🔥 IMPORTANT FIX
        goal.setStatus("NOT_STARTED");

        goalRepository.save(goal);

        redirectAttributes.addFlashAttribute("successMessage",
                "Goal Added Successfully!");

        return "redirect:/employee/goals";
    }
 // ================= UPDATE PROGRESS =================
    @PostMapping("/goals/update-progress/{id}")
    public String updateProgress(@PathVariable Long id,
                                 @RequestParam int progress,
                                 RedirectAttributes redirectAttributes) {

        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        // validation
        if (progress < 0) progress = 0;
        if (progress > 100) progress = 100;

        goal.setProgress(progress);

        goalRepository.save(goal);

        redirectAttributes.addFlashAttribute("successMessage",
                "Progress Updated Successfully!");

        return "redirect:/employee/goals";
    }
 // ================= PERFORMANCE PAGE =================
    @GetMapping("/performance")
    public String performancePage(Model model) {

        Employee employee = getEmployee();

        List<PerformanceReview> reviews =
                performanceReviewRepo.findByEmployee_Id(employee.getId());

        model.addAttribute("reviews", reviews);

        // 🔥 ADD THIS LINE (VERY IMPORTANT)
        model.addAttribute("review", new PerformanceReview());

        return "employee/performance";
    }
    @PostMapping("/performance")
    public String submitReview(@ModelAttribute PerformanceReview review,
                               RedirectAttributes redirectAttributes) {

        Employee employee = getEmployee();

        review.setEmployee(employee);
        performanceReviewRepo.save(review);

        redirectAttributes.addFlashAttribute("successMessage",
                "Performance Review Submitted!");

        return "redirect:/employee/performance";
    }
 // ================= ANNOUNCEMENTS PAGE =================
    @GetMapping("/announcements")
    public String announcementsPage(Model model) {

        List<Announcement> announcements = announcementRepo.findAll();

        model.addAttribute("announcements", announcements);

        return "employee/announcements";
    }
    @GetMapping("/notifications")
    public String notificationsPage(Model model) {

        Employee employee = getEmployee();

        List<Notification> notifications =
                notificationRepo.findByEmployee_IdOrderByCreatedAtDesc(employee.getId());

        long unreadCount =
                notificationRepo.countByEmployee_IdAndStatus(employee.getId(), "UNREAD");

        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);

        return "employee/notifications";
    }
   

        @GetMapping("/holidays")
        public String holidaysPage(Model model) {

            LocalDate today = LocalDate.now();

            List<Holiday> holidays =
                    holidayRepository
                            .findByHolidayDateGreaterThanEqualOrderByHolidayDateAsc(today);

            model.addAttribute("holidays", holidays);

            return "employee/holidays";
        }
    }
