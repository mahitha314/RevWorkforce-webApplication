package com.revworkforce.managercontroller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.managerservice.PerformanceReviewService;
import com.revworkforce.managerservice.TeamGoalsService;
import com.revworkforce.managerservice.TeamLeavesService;
import com.revworkforce.model.Employee;
import com.revworkforce.model.Notification;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.NotificationRepository;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final TeamGoalsService teamGoalsService;
    private final TeamLeavesService teamLeavesService;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final PerformanceReviewService performanceService;
    private final NotificationRepository notificationrepository;

    public ManagerController(TeamGoalsService teamGoalsService,
                             TeamLeavesService teamLeavesService,
                             EmployeeRepository employeeRepository,
                             PasswordEncoder passwordEncoder,
                             PerformanceReviewService performanceService,
                             NotificationRepository notificationrepository) {
        this.teamGoalsService = teamGoalsService;
        this.teamLeavesService = teamLeavesService;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.performanceService = performanceService;
        this.notificationrepository = notificationrepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {

        Employee manager = getLoggedInManager(authentication);
        Long managerId = manager.getId();

        model.addAttribute("view", "dashboard");
        model.addAttribute("manager", manager);
        model.addAttribute("employee", manager);   // add this
        model.addAttribute("managerId", managerId);

        model.addAttribute("teamMembers", employeeRepository.findByManager_Id(managerId));

        ApiResponse leaveResponse = teamLeavesService.getTeamLeaveRequests(managerId);
        model.addAttribute("leaveRequests", leaveResponse != null ? leaveResponse.getData() : null);

        ApiResponse reviewResponse = performanceService.getTeamPerformanceReviews(managerId);
        model.addAttribute("performanceReviews", reviewResponse != null ? reviewResponse.getData() : null);

        ApiResponse goalsResponse = teamGoalsService.getTeamGoals(managerId);
        model.addAttribute("goals", goalsResponse != null ? goalsResponse.getData() : null);

        return "manager/manager-dashboard";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {

        Employee manager = getLoggedInManager(authentication);

        model.addAttribute("view", "profile");
        model.addAttribute("manager", manager);
        model.addAttribute("employee", manager);

        return "manager/profile";
    }

    @GetMapping("/edit-profile")
    public String editProfile(Authentication authentication, Model model) {

        Employee manager = getLoggedInManager(authentication);

        model.addAttribute("view", "profile");
        model.addAttribute("manager", manager);
        model.addAttribute("employee", manager);

        return "manager/edit-profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@RequestParam(required = false) String phoneNumber,
                                @RequestParam(required = false) String address,
                                @RequestParam(required = false) String emergencyContact,
                                @RequestParam(required = false) String currentPassword,
                                @RequestParam(required = false) String newPassword,
                                @RequestParam(required = false) String confirmPassword,
                                Authentication authentication,
                                Model model) {

        Employee existing = getLoggedInManager(authentication);

        existing.setPhoneNumber(phoneNumber);
        existing.setAddress(address);
        existing.setEmergencyContact(emergencyContact);

        if (newPassword != null && !newPassword.isBlank()) {

            if (currentPassword == null || !passwordEncoder.matches(currentPassword, existing.getPassword())) {
                model.addAttribute("view", "profile");
                model.addAttribute("errorMessage", "Current password is incorrect");
                model.addAttribute("manager", existing);
                model.addAttribute("employee", existing);
                return "manager/edit-profile";
            }

            if (confirmPassword == null || !newPassword.equals(confirmPassword)) {
                model.addAttribute("view", "profile");
                model.addAttribute("errorMessage", "Passwords do not match");
                model.addAttribute("manager", existing);
                model.addAttribute("employee", existing);
                return "manager/edit-profile";
            }

            existing.setPassword(passwordEncoder.encode(newPassword));
        }

        employeeRepository.save(existing);

        model.addAttribute("view", "profile");
        model.addAttribute("successMessage", "Profile updated successfully");
        model.addAttribute("manager", existing);
        model.addAttribute("employee", existing);

        return "manager/edit-profile";
    }

    @GetMapping("/team-leaves")
    public String teamLeaves(Authentication authentication, Model model) {

        Employee manager = getLoggedInManager(authentication);

        ApiResponse response = teamLeavesService.getTeamLeaveRequests(manager.getId());

        model.addAttribute("view", "team-leaves");
        model.addAttribute("manager", manager);
        model.addAttribute("employee", manager);
        model.addAttribute("leaves", response.getData());

        return "manager/team_leaves";
    }

    @PostMapping("/approve/{leaveId}")
    public String approve(@PathVariable Long leaveId,
                          @RequestParam(required = false) String comments,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {

        Employee manager = getLoggedInManager(authentication);

        ApiResponse response = teamLeavesService.approveLeave(manager.getId(), leaveId, comments);

        redirectAttributes.addFlashAttribute("sweetMessage", response.getMessage());
        redirectAttributes.addFlashAttribute("sweetStatus", response.getStatus());

        return "redirect:/manager/team-leaves";
    }

    @PostMapping("/reject/{leaveId}")
    public String reject(@PathVariable Long leaveId,
                         @RequestParam String comments,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {

        Employee manager = getLoggedInManager(authentication);

        ApiResponse response = teamLeavesService.rejectLeave(manager.getId(), leaveId, comments);

        redirectAttributes.addFlashAttribute("sweetMessage", response.getMessage());
        redirectAttributes.addFlashAttribute("sweetStatus", response.getStatus());

        return "redirect:/manager/team-leaves";
    }

    @GetMapping("/team-calendar")
    public String calendar(Authentication authentication, Model model) {

        Employee manager = getLoggedInManager(authentication);

        model.addAttribute("view", "team-calendar");
        model.addAttribute("manager", manager);
        model.addAttribute("employee", manager);
        model.addAttribute("calendarLeaves",
                teamLeavesService.getTeamLeaveCalendar(manager.getId()).getData());

        return "manager/team-calendar";
    }

    @GetMapping("/team-balance")
    public String balance(Authentication authentication, Model model) {

        Employee manager = getLoggedInManager(authentication);

        model.addAttribute("view", "team-balance");
        model.addAttribute("manager", manager);
        model.addAttribute("employee", manager);
        model.addAttribute("balances",
                teamLeavesService.getTeamLeaveBalance(manager.getId()).getData());

        return "manager/team-balance";
    }

    @GetMapping("/team-structure")
    public String teamStructure(Authentication authentication, Model model) {

        Employee manager = getLoggedInManager(authentication);

        model.addAttribute("view", "team-structure");
        model.addAttribute("manager", manager);
        model.addAttribute("employee", manager);
        model.addAttribute("managerId", manager.getId());

        return "manager/team_structure";
    }

    @GetMapping("/team-goals")
    public String teamGoals(Authentication authentication, Model model) {

        Employee manager = getLoggedInManager(authentication);

        ApiResponse response = teamGoalsService.getTeamGoals(manager.getId());

        model.addAttribute("view", "team-goals");
        model.addAttribute("manager", manager);
        model.addAttribute("employee", manager);
        model.addAttribute("goals", response.getData());

        return "manager/team_goals";
    }

    @GetMapping("/performance-review")
    public String performancePage(Authentication authentication, Model model) {

        Employee manager = getLoggedInManager(authentication);

        model.addAttribute("view", "performance-review");
        model.addAttribute("manager", manager);
        model.addAttribute("employee", manager);
        model.addAttribute("managerId", manager.getId());

        return "manager/performance_review";
    }

    private Employee getLoggedInManager(Authentication authentication) {
        return employeeRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/notifications")
    public String managerNotifications(Authentication authentication, Model model) {

        Employee manager = employeeRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        List<Notification> notifications =
                notificationrepository.findByEmployee_IdOrderByCreatedAtDesc(manager.getId());

        long unreadCount =
                notificationrepository.countByEmployee_IdAndIsRead(manager.getId(), false);

        model.addAttribute("view", "notifications");
        model.addAttribute("manager", manager);
        model.addAttribute("employee", manager);
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);

        return "manager/notifications";

    }
}