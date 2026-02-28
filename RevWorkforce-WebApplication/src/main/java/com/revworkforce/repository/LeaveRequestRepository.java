package com.revworkforce.repository;

import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveRequest;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository
        extends JpaRepository<LeaveRequest, Long> {

    // ================= PAGINATION =================
    Page<LeaveRequest> findByEmployee(
            Employee employee,
            Pageable pageable
    );

    Page<LeaveRequest> findByEmployeeAndStatus(
            Employee employee,
            String status,
            Pageable pageable
    );

    // ================= NORMAL LIST =================
    List<LeaveRequest> findByEmployee_Id(Long employeeId);

    // ================= SORT NEWEST FIRST =================
    List<LeaveRequest> findByEmployee_IdOrderByStartDateDesc(Long employeeId);

    // ================= UNREAD NOTIFICATIONS COUNT =================
    long countByEmployee_IdAndNotificationReadFalseAndStatusNot(
            Long employeeId,
            String status
    );
}