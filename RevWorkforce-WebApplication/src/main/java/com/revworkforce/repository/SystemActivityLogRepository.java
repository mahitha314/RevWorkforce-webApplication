package com.revworkforce.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revworkforce.model.SystemActivityLog;

public interface SystemActivityLogRepository extends JpaRepository<SystemActivityLog, Long> {

    List<SystemActivityLog> findAllByOrderByCreatedAtDesc();

    List<SystemActivityLog> findByUserNameContainingIgnoreCase(String userName);

    List<SystemActivityLog> findByModule(String module);

    List<SystemActivityLog> findByCreatedAtBetween(
            LocalDateTime start, LocalDateTime end);
}