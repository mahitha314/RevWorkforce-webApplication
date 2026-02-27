package com.revworkforce.managerserviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.logging.AppLogger;
import com.revworkforce.managerservice.NotificationService;
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
	public void sendNotification(Long employeeId, String message) {

	    AppLogger.logInfo("Sending notification to employeeId: " + employeeId);

	    try {

	        Employee employee = employeeRepository.findById(employeeId)
	                .orElseThrow(() -> new RuntimeException("Employee not found"));

	        Notification notification = new Notification();

	        notification.setEmployee(employee);
	        notification.setMessage(message);
	        notification.setStatus("UNREAD");

	        // ✅ CRITICAL FIX
	        notification.setIsRead(false);

	        notification.setCreatedAt(LocalDateTime.now());

	        notificationRepository.save(notification);

	        AppLogger.logInfo("Notification sent successfully");

	    } catch (Exception e) {

	        AppLogger.logError("Error sending notification: " + e.getMessage());
	    }
	}

	@Override
	public List<NotificationDTO> getManagerNotifications(Long managerId) {

		AppLogger.logInfo("Fetching notifications for managerId: " + managerId);

		List<NotificationDTO> dtoList = new ArrayList<>();

		try {

			List<Notification> notificationList = notificationRepository.findByEmployee_Id(managerId);

			for (Notification notification : notificationList) {

				NotificationDTO dto = new NotificationDTO();

				dto.setNotificationId(notification.getNotificationId());
				dto.setMessage(notification.getMessage());
				dto.setCreatedAt(notification.getCreatedAt());

				dtoList.add(dto);
			}

		} catch (Exception e) {

			AppLogger.logError("Error fetching notifications: " + e.getMessage());
		}

		return dtoList;
	}
}