package com.revworkforce.employeeserviceImpl;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.LeaveBalanceDTO;
import com.revworkforce.employeeservice.LeaveBalanceService;
import com.revworkforce.exception.EmployeeNotFoundException;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.LeaveBalanceRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepo;
    private final EmployeeRepository employeeRepo;

    public LeaveBalanceServiceImpl(
            LeaveBalanceRepository leaveBalanceRepo,
            EmployeeRepository employeeRepo) {

        this.leaveBalanceRepo = leaveBalanceRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public ApiResponse getEmployeeBalances(Long employeeId) {

        // ✅ Validate Employee
        var employee = employeeRepo.findById(employeeId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        // ✅ Fetch Leave Balances
        List<LeaveBalanceDTO> balances =
                leaveBalanceRepo.findByEmployee_Id(employeeId)
                        .stream()
                        .map(balance -> new LeaveBalanceDTO(
                                balance.getId(),                                // LeaveBalance ID
                                employee.getId(),                               // Employee ID
                                employee.getFirstName(),                             // Employee Name
                                balance.getLeaveType().getId(),                 // LeaveType ID
                                balance.getLeaveType().getTypeName(),           // LeaveType Name
                                balance.getTotalLeaves(),                       // Total Leaves
                                balance.getUsedLeaves(),                        // Used Leaves
                                balance.getRemainingLeaves()                    // Remaining Leaves
                        ))
                        .collect(Collectors.toList());

        return new ApiResponse(
                200,
                "Leave balances fetched successfully",
                balances
        );
    }
}