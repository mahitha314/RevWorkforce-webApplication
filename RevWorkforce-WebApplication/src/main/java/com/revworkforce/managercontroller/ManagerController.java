package com.revworkforce.managercontroller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.managerservice.TeamLeavesService;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;

@Controller
@RequestMapping("/manager")
public class ManagerController {

	 private final EmployeeRepository employeeRepository;
	    private final PasswordEncoder passwordEncoder;
	    private final TeamLeavesService teamLeavesService;

	    public ManagerController(EmployeeRepository employeeRepository,
	                             PasswordEncoder passwordEncoder,
	                             TeamLeavesService teamLeavesService) {
	        this.employeeRepository = employeeRepository;
	        this.passwordEncoder = passwordEncoder;
	        this.teamLeavesService = teamLeavesService;
	    }

    // ✅ Dashboard Page
    @GetMapping("/dashboard")
    public String managerDashboard(Authentication authentication, Model model) {

        String email = authentication.getName();

        Employee manager = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("manager", manager);
        model.addAttribute("employeeName", manager.getFirstName());
        model.addAttribute("managerId", manager.getId());

        return "manager/manager-dashboard";
    }

    // ✅ Profile Page
    @GetMapping("/profile")
    public String managerProfile(Authentication authentication, Model model) {

        String email = authentication.getName();

        Employee manager = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("manager", manager);

        return "manager/profile";
    }

    // ✅ Update Profile
    @PostMapping("/update-profile")
    public String updateProfile(Employee updatedManager,
                                @RequestParam(required = false) String currentPassword,
                                @RequestParam(required = false) String newPassword,
                                @RequestParam(required = false) String confirmPassword,
                                Authentication authentication,
                                Model model) {

        String email = authentication.getName();

        Employee existingManager = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update basic fields
        existingManager.setEmail(updatedManager.getEmail());
        existingManager.setPhoneNumber(updatedManager.getPhoneNumber());
        existingManager.setAddress(updatedManager.getAddress());

        boolean passwordChanged = false;

        // 🔐 Password Change Logic
        if (newPassword != null && !newPassword.isBlank()) {

            if (!passwordEncoder.matches(currentPassword, existingManager.getPassword())) {
                model.addAttribute("error", "Current password is incorrect");
                model.addAttribute("manager", existingManager);
                return "manager/edit-profile";
            }

            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error", "New passwords do not match");
                model.addAttribute("manager", existingManager);
                return "manager/edit-profile";
            }

            existingManager.setPassword(passwordEncoder.encode(newPassword));
            passwordChanged = true;
        }

        employeeRepository.save(existingManager);

        if (passwordChanged) {
            return "redirect:/manager/profile?passwordSuccess";
        } else {
            return "redirect:/manager/profile?profileSuccess";
        }
    }
 // ✅ Edit Profile Page
    @GetMapping("/edit-profile")
    public String editProfile(Authentication authentication, Model model) {

        String email = authentication.getName();

        Employee manager = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("manager", manager);

        return "manager/edit-profile";
    }
    
    
    
    // ================= TEAM LEAVES =================

    @GetMapping("/team-leaves")
    public String viewTeamLeaves(Authentication authentication, Model model) {

        Employee manager = employeeRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ApiResponse response =
                teamLeavesService.getTeamLeaveRequests(manager.getId());

        model.addAttribute("managerId", manager.getId());
        model.addAttribute("leaves", response.getData());

        return "manager/team-leaves";
    }

    @PostMapping("/approve/{leaveId}")
    public String approveLeave(@PathVariable Long leaveId,
                               @RequestParam(required = false) String comments,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {

        Employee manager = employeeRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ApiResponse response =
                teamLeavesService.approveLeave(manager.getId(), leaveId, comments);

        redirectAttributes.addFlashAttribute("sweetMessage", response.getMessage());
        redirectAttributes.addFlashAttribute("sweetStatus", response.getStatus());

        return "redirect:/manager/team-leaves";
    }
    @PostMapping("/reject/{leaveId}")
    public String rejectLeave(@PathVariable Long leaveId,
                              @RequestParam String comments,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {

        Employee manager = employeeRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ApiResponse response =
                teamLeavesService.rejectLeave(manager.getId(), leaveId, comments);

        redirectAttributes.addFlashAttribute("sweetMessage", response.getMessage());
        redirectAttributes.addFlashAttribute("sweetStatus", response.getStatus());

        return "redirect:/manager/team-leaves";
    }

    @GetMapping("/team-calendar")
    public String teamCalendar(Authentication authentication, Model model) {

        Employee manager = employeeRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ApiResponse response =
                teamLeavesService.getTeamLeaveCalendar(manager.getId());

        model.addAttribute("calendarLeaves", response.getData());

        return "manager/team-calendar";
    }

    @GetMapping("/team-balance")
    public String teamBalance(Authentication authentication, Model model) {

        Employee manager = employeeRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ApiResponse response =
                teamLeavesService.getTeamLeaveBalance(manager.getId());

        model.addAttribute("balances", response.getData());

        return "manager/team-balance";
    }
    
}