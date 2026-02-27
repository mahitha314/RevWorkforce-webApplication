package com.revworkforce.adminserviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.revworkforce.adminservice.NotificationService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.model.Employee;
import com.revworkforce.model.Notification;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.NotificationRepository;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	private final NotificationRepository notificationRepository;
    private final EmployeeRepository employeeRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   EmployeeRepository employeeRepository) {
        this.notificationRepository = notificationRepository;
        this.employeeRepository = employeeRepository;
    }
    
 // 🔔 Create Notification
    @Override
    public ApiResponse createNotification(NotificationDTO dto) {

        Employee employee = employeeRepository
                .findById(dto.getEmployeeId())
                .orElse(null);

        if (employee == null) {
            return new ApiResponse(404, "Employee not found", null);
        }

        Notification notification = new Notification();
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setType(dto.getType());
        notification.setStatus(dto.getStatus());
        notification.setIsRead(dto.getIsRead());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setEmployee(employee);
        notification.setReferenceId(dto.getReferenceId());

        notificationRepository.save(notification);

        return new ApiResponse(201, "Notification created successfully", null);
    }
    
 // 📩 Get all notifications
    @Override
    public ApiResponse getEmployeeNotifications(Long employeeId) {

        List<Notification> notifications =
                notificationRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId);

        List<NotificationDTO> dtoList = notifications.stream()
                .map(n -> {
                    NotificationDTO dto = new NotificationDTO();
                    dto.setNotificationId(n.getNotificationId());
                    dto.setTitle(n.getTitle());
                    dto.setMessage(n.getMessage());
                    dto.setType(n.getType());
                    dto.setStatus(n.getStatus());
                    dto.setIsRead(n.getIsRead());
                    dto.setCreatedAt(n.getCreatedAt());
                    dto.setReferenceId(n.getReferenceId());
                    return dto;
                }).collect(Collectors.toList());

        return new ApiResponse(200, "Notifications fetched", dtoList);
    }

    // 🔢 Unread count
    @Override
    public ApiResponse getUnreadCount(Long employeeId) {

        long count = notificationRepository
                .countByEmployeeIdAndIsRead(employeeId, false);

        return new ApiResponse(200, "Unread count", count);
    }

    // ✅ Mark one as read
    @Override
    public ApiResponse markAsRead(Long notificationId) {

        Notification notification =
                notificationRepository.findById(notificationId).orElse(null);

        if (notification == null) {
            return new ApiResponse(404, "Notification not found", null);
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);

        return new ApiResponse(200, "Notification marked as read", null);
    }

    // ✅ Mark all as read
    @Override
    public ApiResponse markAllAsRead(Long employeeId) {

        List<Notification> list =
                notificationRepository.findByEmployeeIdAndIsRead(employeeId, false);

        list.forEach(n -> n.setIsRead(true));

        notificationRepository.saveAll(list);

        return new ApiResponse(200, "All notifications marked as read", null);
    }

    // ❌ Delete notification
    @Override
    public ApiResponse deleteNotification(Long notificationId) {

        if (!notificationRepository.existsById(notificationId)) {
            return new ApiResponse(404, "Notification not found", null);
        }

        notificationRepository.deleteById(notificationId);

        return new ApiResponse(200, "Notification deleted successfully", null);
    }
    
}