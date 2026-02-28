package com.revworkforce.notification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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

	@Override
	public ApiResponse createNotification(NotificationDTO dto) {

		Employee employee = employeeRepository.findByEmployeeId(dto.getEmployeeId()).orElse(null);

		if (employee == null) {
			return new ApiResponse(404, "Employee not found", null);
		}

		Notification notification = new Notification();
		notification.setEmployee(employee);
		notification.setTitle(dto.getTitle());
		notification.setMessage(dto.getMessage());
		notification.setType(dto.getType());
		notification.setStatus(dto.getStatus());
		notification.setIsRead(false);
		notification.setCreatedAt(LocalDateTime.now());
		notification.setReferenceId(dto.getReferenceId());

		notificationRepository.save(notification);

		return new ApiResponse(201, "Notification created successfully", null);
	}

	@Override
	public ApiResponse getMyNotifications() {

		Employee employee = getLoggedInEmployee();

		List<NotificationDTO> notifications = notificationRepository
				.findByEmployeeEmployeeIdOrderByCreatedAtDesc(employee.getEmployeeId()).stream().map(this::convertToDTO)
				.collect(Collectors.toList());

		return new ApiResponse(200, "Notifications fetched successfully", notifications);
	}

	@Override
	public ApiResponse getUnreadCount() {
		Employee employee = getLoggedInEmployee();

		long count = notificationRepository.countByEmployeeEmployeeIdAndIsRead(employee.getEmployeeId(), false);

		return new ApiResponse(200, "Unread count fetched", count);
	}

	@Override
	public ApiResponse markAsRead(Long notificationId) {

		Notification notification = notificationRepository.findById(notificationId).orElse(null);

		if (notification == null) {
			return new ApiResponse(404, "Notification not found", null);
		}

		Employee employee = getLoggedInEmployee();

		if (!notification.getEmployee().getId().equals(employee.getId())) {

			return new ApiResponse(403, "Unauthorized access", null);
		}

		notification.setIsRead(true);
		notificationRepository.save(notification);

		return new ApiResponse(200, "Notification marked as read", null);
	}

	@Override
	public ApiResponse markAllAsRead() {

		Employee employee = getLoggedInEmployee();

		List<Notification> list = notificationRepository.findByEmployeeEmployeeIdAndIsRead(employee.getEmployeeId(),
				false);

		list.forEach(n -> n.setIsRead(true));

		notificationRepository.saveAll(list);

		return new ApiResponse(200, "All notifications marked as read", null);
	}

	@Override
	public ApiResponse deleteNotification(Long notificationId) {

		if (!notificationRepository.existsById(notificationId)) {
			return new ApiResponse(404, "Notification not found", null);
		}

		notificationRepository.deleteById(notificationId);

		return new ApiResponse(200, "Notification deleted successfully", null);
	}

	private Employee getLoggedInEmployee() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String email = auth.getName();

		return employeeRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
	}

	private NotificationDTO convertToDTO(Notification notification) {

		NotificationDTO dto = new NotificationDTO();
		dto.setNotificationId(notification.getNotificationId());
		dto.setEmployeeId(notification.getEmployee().getEmployeeId());
		dto.setTitle(notification.getTitle());
		dto.setMessage(notification.getMessage());
		dto.setType(notification.getType());
		dto.setStatus(notification.getStatus());
		dto.setIsRead(notification.getIsRead());
		dto.setCreatedAt(notification.getCreatedAt());
		dto.setReferenceId(notification.getReferenceId());

		return dto;
	}

}