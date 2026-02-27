package com.revworkforce.employeeserviceImpl;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.LeaveRequestDTO;
import com.revworkforce.employeeservice.LeaveRequestService;
import com.revworkforce.exception.EmployeeNotFoundException;
import com.revworkforce.exception.LeaveNotAllowedException;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveBalance;
import com.revworkforce.model.LeaveRequest;
import com.revworkforce.model.LeaveType;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.LeaveBalanceRepository;
import com.revworkforce.repository.LeaveRequestRepository;
import com.revworkforce.repository.LeaveTypeRepository;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

import java.time.temporal.ChronoUnit;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepo;
    private final LeaveBalanceRepository leaveBalanceRepo;
    private final LeaveTypeRepository leaveTypeRepo;
    private final EmployeeRepository employeeRepo;

    public LeaveRequestServiceImpl(
            LeaveRequestRepository leaveRequestRepo,
            LeaveBalanceRepository leaveBalanceRepo,
            LeaveTypeRepository leaveTypeRepo,
            EmployeeRepository employeeRepo) {

        this.leaveRequestRepo = leaveRequestRepo;
        this.leaveBalanceRepo = leaveBalanceRepo;
        this.leaveTypeRepo = leaveTypeRepo;
        this.employeeRepo = employeeRepo;
    }

    // ===================================================
    // APPLY LEAVE
    // ===================================================
    @Override
    public ApiResponse applyLeave(LeaveRequestDTO dto) {

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new LeaveNotAllowedException("End date cannot be before start date");
        }

        Employee employee = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found"));

        LeaveType leaveType = leaveTypeRepo.findById(dto.getLeaveTypeId())
                .orElseThrow(() ->
                        new LeaveNotAllowedException("Leave type not found"));

        LeaveBalance balance = leaveBalanceRepo
                .findByEmployee_IdAndLeaveType_Id(dto.getEmployeeId(), dto.getLeaveTypeId())
                .orElseThrow(() ->
                        new LeaveNotAllowedException("Leave balance not found"));

        long days = ChronoUnit.DAYS.between(
                dto.getStartDate(),
                dto.getEndDate()
        ) + 1;

        if (balance.getRemainingLeaves() < days) {
            throw new LeaveNotAllowedException("Insufficient leave balance");
        }

        LeaveRequest request = new LeaveRequest();
        request.setEmployee(employee);
        request.setLeaveType(leaveType);
        request.setStartDate(dto.getStartDate());
        request.setEndDate(dto.getEndDate());
        request.setReason(dto.getReason());
        request.setStatus("PENDING");

        leaveRequestRepo.save(request);

        return new ApiResponse(
                200,
                "Leave applied successfully",
                request.getId()
        );
    }

    // ===================================================
    // CANCEL LEAVE
    // ===================================================
    @Override
    public ApiResponse cancelLeave(Long leaveId) {

        LeaveRequest request = leaveRequestRepo.findById(leaveId)
                .orElseThrow(() ->
                        new LeaveNotAllowedException("Leave not found"));

        if (!"PENDING".equalsIgnoreCase(request.getStatus())) {
            throw new LeaveNotAllowedException("Only pending leave can be cancelled");
        }

        request.setStatus("CANCELLED");
        leaveRequestRepo.save(request);

        return new ApiResponse(
                200,
                "Leave cancelled successfully",
                null
        );
    }

    // ===================================================
    // LEAVE HISTORY → ALL LEAVES
    // ===================================================
    @Override
    public ApiResponse getLeaveHistory(Long employeeId, int page, int size) {

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found"));

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("startDate").descending());

        Page<LeaveRequest> history =
                leaveRequestRepo.findByEmployee(employee, pageable);

        return new ApiResponse(
                200,
                "Leave history fetched successfully",
                history
        );
    }

    // ===================================================
    // LEAVE STATUS → ONLY PENDING
    // ===================================================
    @Override
    public ApiResponse getLeaveStatus(Long employeeId, int page, int size) {

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found"));

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("startDate").descending());

        Page<LeaveRequest> pendingLeaves =
                leaveRequestRepo.findByEmployeeAndStatus(
                        employee,
                        "PENDING",
                        pageable
                );

        return new ApiResponse(
                200,
                "Leave status fetched successfully",
                pendingLeaves
        );
    }
}