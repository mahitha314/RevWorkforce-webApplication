package com.revworkforce.repository;

import com.revworkforce.model.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    List<LeaveBalance> findByEmployee_Id(Long employeeId);


    Optional<LeaveBalance> findByEmployee_IdAndLeaveType_Id(Long employeeId, Long leaveTypeId);
}