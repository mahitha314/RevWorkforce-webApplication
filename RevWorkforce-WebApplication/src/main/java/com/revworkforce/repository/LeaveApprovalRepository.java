package com.revworkforce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revworkforce.model.LeaveApproval;

public interface LeaveApprovalRepository extends JpaRepository<LeaveApproval, Long> {
}