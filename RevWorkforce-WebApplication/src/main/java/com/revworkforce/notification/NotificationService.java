package com.revworkforce.notification;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.NotificationDTO;

public interface NotificationService {

    ApiResponse createNotification(NotificationDTO dto);

    ApiResponse createNotificationForAll(NotificationDTO dto); // Admin broadcast

    ApiResponse getMyNotifications();

    ApiResponse getUnreadCount();

    ApiResponse markAsRead(Long notificationId);

    ApiResponse markAllAsRead();

    ApiResponse deleteNotification(Long notificationId);

}