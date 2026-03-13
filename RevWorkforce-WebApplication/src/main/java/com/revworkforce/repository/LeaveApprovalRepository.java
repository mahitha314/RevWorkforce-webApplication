package com.revworkforce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.revworkforce.model.LeaveApproval;

@Repository
public interface LeaveApprovalRepository extends JpaRepository<LeaveApproval, Long> {

}