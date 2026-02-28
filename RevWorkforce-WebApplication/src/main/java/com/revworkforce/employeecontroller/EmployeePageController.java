package com.revworkforce.employeecontroller;

import com.revworkforce.model.*;
import com.revworkforce.repository.*;
import com.revworkforce.employeeservice.GoalService;

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
    private final GoalService goalService;
    private final AnnouncementRepository announcementRepo;
    private final NotificationRepository notificationRepo;

    public EmployeePageController(EmployeeRepository employeeRepo,
                                  LeaveBalanceRepository leaveBalanceRepo,
                                  LeaveTypeRepository leaveTypeRepo,
                                  LeaveRequestRepository leaveRequestRepo,
                                  GoalRepository goalRepository,
                                  GoalService goalService,
                                  AnnouncementRepository announcementRepo, 
                                  NotificationRepository notificationRepo) {

        this.employeeRepo = employeeRepo;
        this.leaveBalanceRepo = leaveBalanceRepo;
        this.leaveTypeRepo = leaveTypeRepo;
        this.leaveRequestRepo = leaveRequestRepo;
        this.goalRepository = goalRepository;
        this.goalService = goalService;
        this.announcementRepo = announcementRepo;
        this.notificationRepo = notificationRepo;
    }

    // ================= GET LOGGED EMPLOYEE =================
    private Employee getEmployee() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return employeeRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // ================= GLOBAL UNREAD COUNT (FOR BELL) =================


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

        int totalAllocated = 0;
        int totalUsed = 0;
        int totalRemaining = 0;
        int pendingCount = 0;

        for (LeaveBalance b : balances) {
            totalAllocated += b.getTotalLeaves();
            totalUsed += b.getUsedLeaves();
            totalRemaining += b.getRemainingLeaves();
        }

        for (LeaveRequest r : leaveRequests) {
            if ("PENDING".equalsIgnoreCase(r.getStatus())) {
                pendingCount++;
            }
        }

        model.addAttribute("employee", employee);
        model.addAttribute("balances", balances);
        model.addAttribute("leaveRequests", leaveRequests);
        model.addAttribute("goals", goals);
        model.addAttribute("totalAllocated", totalAllocated);
        model.addAttribute("totalUsed", totalUsed);
        model.addAttribute("totalRemaining", totalRemaining);
        model.addAttribute("pendingCount", pendingCount);

        return "employee/dashboard";
    }

    // ================= PROFILE =================
    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("employee", getEmployee());
        return "employee/profile";
    }

    // ================= APPLY LEAVE =================
    @GetMapping("/apply-leave")
    public String applyLeavePage(Model model) {
        model.addAttribute("employee", getEmployee());
        model.addAttribute("leaveTypes", leaveTypeRepo.findAll());
        return "employee/apply_leave";
    }

    @PostMapping("/apply-leave")
    public String applyLeave(@RequestParam Long leaveTypeId,
                             @RequestParam String startDate,
                             @RequestParam String endDate,
                             @RequestParam String reason,
                             RedirectAttributes redirectAttributes) {

        Employee employee = getEmployee();

        LeaveType leaveType =
                leaveTypeRepo.findById(leaveTypeId)
                        .orElseThrow(() -> new RuntimeException("Leave type not found"));

        LeaveRequest request = new LeaveRequest();
        request.setEmployee(employee);
        request.setLeaveType(leaveType);
        request.setStartDate(LocalDate.parse(startDate));
        request.setEndDate(LocalDate.parse(endDate));
        request.setReason(reason);
        request.setStatus("PENDING");
        request.setNotificationRead(false);

        leaveRequestRepo.save(request);

        redirectAttributes.addFlashAttribute("successMessage",
                "Leave Applied Successfully!");

        return "redirect:/employee/leave-status";
    }

    // ================= LEAVE STATUS =================
    @GetMapping("/leave-status")
    public String leaveStatus(Model model) {

        Employee employee = getEmployee();

        model.addAttribute("employee", employee);
        model.addAttribute("leaveRequests",
                leaveRequestRepo.findByEmployee_Id(employee.getId()));

        return "employee/leave_status";
    }

    // ================= LEAVE BALANCE =================
    @GetMapping("/leave-balance")
    public String leaveBalance(Model model) {

        Employee employee = getEmployee();

        model.addAttribute("employee", employee);
        model.addAttribute("balances",
                leaveBalanceRepo.findByEmployee_Id(employee.getId()));

        return "employee/leave_balance";
    }

    @GetMapping("/leave-history")
    public String leaveHistory(Model model) {

        Employee employee = getEmployee();

        List<LeaveRequest> history =
                leaveRequestRepo.findByEmployee_Id(employee.getId());

        model.addAttribute("employee", employee);
        model.addAttribute("history", history);

        return "employee/leave_history";
    }

    // ================= ANNOUNCEMENTS =================
    @GetMapping("/announcements")
    public String announcements(Model model) {

        model.addAttribute("employee", getEmployee());
        model.addAttribute("announcements",
                announcementRepo.findAllByOrderByPostedDateDesc());

        return "employee/announcements";
    }

    // ================= NOTIFICATIONS =================
    @GetMapping("/notifications")
    public String notifications(Model model) {

        Employee employee = getEmployee();

        model.addAttribute("employee", employee);
        model.addAttribute("leaveRequests",
                leaveRequestRepo
                        .findByEmployee_IdOrderByStartDateDesc(employee.getId()));

        return "employee/notifications";
    }

    // ================= MARK NOTIFICATION AS READ =================
    @PostMapping("/notifications/read/{id}")
    public String markNotificationRead(@PathVariable Long id) {

        LeaveRequest leave =
                leaveRequestRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Leave not found"));

        leave.setNotificationRead(true);
        leaveRequestRepo.save(leave);

        return "redirect:/employee/notifications";
    }

    // ================= CANCEL LEAVE =================
    @PostMapping("/cancel-leave/{id}")
    public String cancelLeave(@PathVariable Long id,
                              RedirectAttributes redirectAttributes) {

        Employee employee = getEmployee();

        LeaveRequest leave =
                leaveRequestRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Leave not found"));

        if (!leave.getEmployee().getId().equals(employee.getId())) {
            throw new RuntimeException("Unauthorized action");
        }

        if ("PENDING".equalsIgnoreCase(leave.getStatus())) {
            leave.setStatus("CANCELLED");
            leave.setNotificationRead(false);
            leaveRequestRepo.save(leave);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Leave Cancelled Successfully!");
        }

        return "redirect:/employee/leave-status";
    }
    @ModelAttribute("unreadCount")
    public long unreadCount() {

        Employee emp = getEmployee();

        return notificationRepo
                .countByEmployee_IdAndStatus(emp.getId(), "UNREAD");
    }
    @GetMapping("/directory")
    public String employeeDirectory(@RequestParam(required = false) String keyword,
                                    Model model) {

        Employee loggedEmployee = getEmployee();
        List<Employee> employees;

        if (keyword != null && !keyword.trim().isEmpty()) {

            String[] parts = keyword.trim().split(" ");

            if (parts.length == 2) {
                // If user typed first + last name
                employees = employeeRepo
                        .findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
                                parts[0], parts[1]);
            } else {
                // Single word search
                employees = employeeRepo
                        .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                                keyword, keyword, keyword);
            }

        } else {
            employees = employeeRepo.findAll();
        }

        model.addAttribute("employee", loggedEmployee);
        model.addAttribute("employees", employees);
        model.addAttribute("keyword", keyword);

        return "employee/directory";
    }
}