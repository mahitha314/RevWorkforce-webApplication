package com.workforce.repository;

import com.workforce.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository 
        extends JpaRepository<Notification, Long> {

    // Get notifications by employee
    List<Notification> findByEmployeeId(Long employeeId);

    // Get unread notifications
    List<Notification> findByEmployeeIdAndIsReadFalse(Long employeeId);
}