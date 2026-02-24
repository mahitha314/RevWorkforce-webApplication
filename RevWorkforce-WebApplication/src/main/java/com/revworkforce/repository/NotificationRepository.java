package com.revworkforce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revworkforce.model.Notification;

import java.util.List;

@Repository
public interface NotificationRepository 
        extends JpaRepository<Notification, Long> {

    // Get notifications by employee
    List<Notification> findByEmployeeId(Long employeeId);

    // Get unread notifications
    List<Notification> findByEmployeeIdAndIsReadFalse(Long employeeId);
}