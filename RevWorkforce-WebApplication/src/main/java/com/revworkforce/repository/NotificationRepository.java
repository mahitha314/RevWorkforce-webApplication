package com.revworkforce.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.revworkforce.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByEmployeeEmployeeIdOrderByCreatedAtDesc(String employeeId);

	List<Notification> findByEmployeeEmployeeIdAndIsRead(String employeeId, Boolean isRead);

	List<Notification> findAllByOrderByCreatedAtDesc();

	long countByEmployeeEmployeeIdAndIsRead(String employeeId, Boolean isRead);

}