package com.revworkforce.notification;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.NotificationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/my")
    public ResponseEntity<ApiResponse> getMyNotification() {
        ApiResponse response = notificationService.getMyNotifications();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("my/unread-count")
    public ResponseEntity<ApiResponse> getunreadCount() {
        ApiResponse response = notificationService.getUnreadCount();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse> markAsRead(@PathVariable Long notificationId) {
        ApiResponse response = notificationService.markAsRead(notificationId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse> markAllAsRead() {
        ApiResponse response = notificationService.markAllAsRead();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long notificationId) {
        ApiResponse response = notificationService.deleteNotification(notificationId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    
}