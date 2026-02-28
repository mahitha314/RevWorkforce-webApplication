package com.revworkforce.notification;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.NotificationDTO;

public interface NotificationService {
	
	ApiResponse createNotification(NotificationDTO dto);

    ApiResponse getMyNotifications();

    ApiResponse getUnreadCount();

    ApiResponse markAsRead(Long notificationId);

    ApiResponse markAllAsRead();

    ApiResponse deleteNotification(Long notificationId);
	
}