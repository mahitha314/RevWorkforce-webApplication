package com.revworkforce.repository;

import com.revworkforce.model.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveTypeRepository
        extends JpaRepository<LeaveType, Long> {
}