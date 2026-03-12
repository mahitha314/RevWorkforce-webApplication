package com.revworkforce.notification;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.model.Employee;
import com.revworkforce.model.Notification;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.NotificationRepository;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmployeeRepository employeeRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   EmployeeRepository employeeRepository) {
        this.notificationRepository = notificationRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public ApiResponse createNotification(NotificationDTO dto) {

        Employee employee = employeeRepository
                .findByEmployeeId(dto.getEmployeeId())
                .orElse(null);

        if (employee == null) {
            return new ApiResponse(404, "Employee not found", null);
        }

        Notification notification = buildNotification(employee, dto);
        notificationRepository.save(notification);

        return new ApiResponse(201, "Notification created successfully", null);
    }

    @Override
    public ApiResponse createNotificationForAll(NotificationDTO dto) {

        List<Employee> employees = employeeRepository.findAll();

        List<Notification> list = employees.stream()
                .map(emp -> buildNotification(emp, dto))
                .collect(Collectors.toList());

        notificationRepository.saveAll(list);

        return new ApiResponse(201, "Notification sent to all employees", null);
    }

    @Override
    public ApiResponse getMyNotifications() {

        Employee employee = getLoggedInUser();

        List<NotificationDTO> notifications =
                notificationRepository
                        .findByEmployee_IdOrderByCreatedAtDesc(
                                employee.getId()
                        )
                        .stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());

        return new ApiResponse(200, "Notifications fetched successfully", notifications);
    }

    public ApiResponse getUnreadCount() {

        Employee employee = getLoggedInUser();

        long count = notificationRepository
        		.countByEmployee_IdAndIsRead(employee.getId(), false);

        return new ApiResponse(200, "Unread count fetched", count);
    }

    @Override
    public ApiResponse markAsRead(Long notificationId) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElse(null);

        if (notification == null) {
            return new ApiResponse(404, "Notification not found", null);
        }

        Employee employee = getLoggedInUser();

        if (!notification.getEmployee().getId().equals(employee.getId())) {
            return new ApiResponse(403, "Unauthorized access", null);
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);

        return new ApiResponse(200, "Notification marked as read", null);
    }

    @Override
    public ApiResponse markAllAsRead() {

        Employee employee = getLoggedInUser();

        List<Notification> list =
                notificationRepository
                .findByEmployee_IdAndIsRead(employee.getId(), false);

        list.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(list);

        return new ApiResponse(200, "All notifications marked as read", null);
    }

    @Override
    public ApiResponse deleteNotification(Long notificationId) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElse(null);

        if (notification == null) {
            return new ApiResponse(404, "Notification not found", null);
        }

        Employee employee = getLoggedInUser();

        if (!notification.getEmployee().getId().equals(employee.getId())) {
            return new ApiResponse(403, "Unauthorized access", null);
        }

        notificationRepository.delete(notification);

        return new ApiResponse(200, "Notification deleted successfully", null);
    }

    private Notification buildNotification(Employee employee, NotificationDTO dto) {

        Notification notification = new Notification();
        notification.setEmployee(employee);
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setType(dto.getType());
        notification.setStatus(dto.getStatus());
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReferenceId(dto.getReferenceId());

        return notification;
    }

    private Employee getLoggedInUser() {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = auth.getName();

        if (email == null) {
            throw new RuntimeException("User not authenticated");
        }

        final String loginEmail = email.trim().toLowerCase(Locale.ROOT);

        return employeeRepository
                .findByEmailIgnoreCase(loginEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found in employees table: " + loginEmail));
        
    }

    private NotificationDTO convertToDTO(Notification notification) {

        return new NotificationDTO(
                notification.getNotificationId(),
                notification.getEmployee().getEmployeeId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.getStatus(),
                notification.getIsRead(),
                notification.getCreatedAt(),
                notification.getReferenceId()
        );
    }

}