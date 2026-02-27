package com.revworkforce.managerservice;

import com.revworkforce.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {

    void sendNotification(Long employeeId, String message);

    List<NotificationDTO> getManagerNotifications(Long managerId);

}