package com.revworkforce.notification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.NotificationDTO;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // 🔹 Create Notification
    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody NotificationDTO dto) {
        return ResponseEntity.ok(notificationService.createNotification(dto));
    }

    // 🔹 Broadcast
    @PostMapping("/broadcast")
    public ResponseEntity<ApiResponse> broadcast(@RequestBody NotificationDTO dto) {
        return ResponseEntity.ok(notificationService.createNotificationForAll(dto));
    }

    // 🔹 Get My Notifications
    @GetMapping("/my")
    public ResponseEntity<ApiResponse> getMyNotifications() {
        return ResponseEntity.ok(notificationService.getMyNotifications());
    }

    // 🔹 Unread Count
    @GetMapping("/my/unread-count")
    public ResponseEntity<ApiResponse> getUnreadCount() {
        return ResponseEntity.ok(notificationService.getUnreadCount());
    }

    // 🔹 Mark As Read
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse> markAsRead(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.markAsRead(notificationId));
    }

    // 🔹 Mark All As Read
    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse> markAllAsRead() {
        return ResponseEntity.ok(notificationService.markAllAsRead());
    }

    // 🔹 Delete
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.deleteNotification(notificationId));
    }
}