package com.revworkforce.notification;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.revworkforce.dto.ApiResponse;

@Controller
@RequestMapping("/ui/notifications")
public class NotificationsController {

    private final NotificationService notificationService;

    public NotificationsController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public String notificationsPage(Model model, Authentication authentication) {
    	
    	model.addAttribute("pageTitle", "Notifications");
        model.addAttribute("view", "notifications"); // ✅ add this

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
//        model.addAttribute("pageTitle", "Notifications");
//        model.addAttribute("view", "notifications");
//        // Load notifications
//        ApiResponse notifications = notificationService.getMyNotifications();
//        model.addAttribute("notifications", notifications.getData());
//
//        // Load unread count
//        ApiResponse unreadCount = notificationService.getUnreadCount();
//        model.addAttribute("unreadCount", unreadCount.getData());
//
//        // 🔥 ROLE BASED TEMPLATE RETURN
//        if (authentication.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
//
//            return "admin/notifications";
//        }
//
//        if (authentication.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
//
//            return "manager/notifications";
//        }
//
//        if (authentication.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"))) {
//
//            return "employee/notifications";
//        }
//
//        // fallback
//        return "redirect:/login";
    }
}