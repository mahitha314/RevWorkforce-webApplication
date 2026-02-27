package com.revworkforce.repository;

import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository
        extends JpaRepository<LeaveRequest, Long> {

    Page<LeaveRequest> findByEmployee(
            Employee employee,
            Pageable pageable
    );

    Page<LeaveRequest> findByEmployeeAndStatus(
            Employee employee,
            String status,
            Pageable pageable
    );
}