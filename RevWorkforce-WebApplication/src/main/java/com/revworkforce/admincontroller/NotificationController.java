package com.revworkforce.admincontroller;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.adminservice.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody NotificationDTO dto) {
        ApiResponse response = notificationService.createNotification(dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<ApiResponse> getAll(@PathVariable Long employeeId) {
        ApiResponse response = notificationService.getEmployeeNotifications(employeeId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/unread/{employeeId}")
    public ResponseEntity<ApiResponse> unreadCount(@PathVariable Long employeeId) {
        ApiResponse response = notificationService.getUnreadCount(employeeId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/read/{notificationId}")
    public ResponseEntity<ApiResponse> markRead(@PathVariable Long notificationId) {
        ApiResponse response = notificationService.markAsRead(notificationId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/read-all/{employeeId}")
    public ResponseEntity<ApiResponse> markAllRead(@PathVariable Long employeeId) {
        ApiResponse response = notificationService.markAllAsRead(employeeId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long notificationId) {
        ApiResponse response = notificationService.deleteNotification(notificationId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    
}