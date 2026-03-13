package com.revworkforce.notification;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.revworkforce.adminservice.AdminService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.model.Employee;

@Controller
@RequestMapping("/ui/notifications")
public class NotificationsController {

    private final NotificationService notificationService;
    private final AdminService adminService;

    public NotificationsController(NotificationService notificationService,
                                   AdminService adminService) {
        this.notificationService = notificationService;
        this.adminService = adminService;
    }

    @GetMapping
    public String notificationsPage(Model model, Authentication authentication) {

        model.addAttribute("pageTitle", "Notifications");
        model.addAttribute("view", "notifications");

        String email = authentication.getName();

        Employee employee = adminService.getEmployeeByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("employee", employee);

        ApiResponse notifications = notificationService.getMyNotifications();
        model.addAttribute("notifications", notifications.getData());

        ApiResponse unreadCount = notificationService.getUnreadCount();
        model.addAttribute("unreadCount", unreadCount.getData());

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "admin/notifications";
        }

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
            return "manager/notifications";
        }

        return "employee/notifications";
    }
}