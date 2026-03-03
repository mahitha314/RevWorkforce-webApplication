package com.revworkforce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revworkforce.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {


    List<Notification> findAllByOrderByCreatedAtDesc();
    
    long countByEmployeeEmployeeIdAndIsRead(String employeeId, Boolean isRead);
    
    List<Notification> findByEmployee_IdOrderByCreatedAtDesc(Long employeeId);

	long countByEmployee_IdAndStatus(Long employeeId, String status);
	List<Notification> getNotificationsByEmployee_IdOrderByCreatedAtDesc(Long employeeId);

	long countByEmployee_IdAndIsRead(Long id, Boolean isRead);
	List<Notification> findByEmployee_IdAndIsRead(Long id, Boolean isRead);
	
}