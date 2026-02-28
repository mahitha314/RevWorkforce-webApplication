package com.revworkforce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revworkforce.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByEmployeeEmployeeIdOrderByCreatedAtDesc(String employeeId);

    List<Notification> findAllByOrderByCreatedAtDesc();
    List<Notification> findByEmployeeIdAndIsRead(Long employeeId, Boolean isRead);
    long countByEmployeeIdAndIsRead(Long employeeId, Boolean isRead);
}