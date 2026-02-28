package com.revworkforce.repository;

import com.revworkforce.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    // Newest first
    List<Notification> findByEmployee_IdOrderByCreatedAtDesc(Long employeeId);

    // Bell count
    long countByEmployee_IdAndStatus(Long employeeId, String status);
}