package com.revworkforce.managercontroller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.logging.AppLogger;
import com.revworkforce.managerservice.NotificationService;

@RestController
@RequestMapping("/manager/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * GET Notifications for Manager
     * URL: /manager/notifications/{managerId}
     */
    @GetMapping("/{managerId}")
    public ResponseEntity<List<NotificationDTO>> getManagerNotifications(
            @PathVariable Long managerId) {

        AppLogger.logInfo("Fetching notifications for managerId: " + managerId);

        return ResponseEntity.ok(
                notificationService.getManagerNotifications(managerId));
    }
}