package com.revworkforce.adminservice;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.NotificationDTO;

public interface NotificationService {

    ApiResponse createNotification(NotificationDTO dto);

    ApiResponse getEmployeeNotifications(Long employeeId);

    ApiResponse getUnreadCount(Long employeeId);

    ApiResponse markAsRead(Long notificationId);

    ApiResponse markAllAsRead(Long employeeId);

    ApiResponse deleteNotification(Long notificationId);
    
}