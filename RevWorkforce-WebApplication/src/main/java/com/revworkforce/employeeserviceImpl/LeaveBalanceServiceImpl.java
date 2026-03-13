package com.revworkforce.employeeserviceImpl;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.LeaveBalanceDTO;
import com.revworkforce.employeeservice.LeaveBalanceService;
import com.revworkforce.exception.EmployeeNotFoundException;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.LeaveBalanceRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private static final Logger logger =
            LoggerFactory.getLogger(LeaveBalanceServiceImpl.class);

    private final LeaveBalanceRepository leaveBalanceRepo;
    private final EmployeeRepository employeeRepo;

    public LeaveBalanceServiceImpl(LeaveBalanceRepository leaveBalanceRepo,
                                   EmployeeRepository employeeRepo) {

        this.leaveBalanceRepo = leaveBalanceRepo;
        this.employeeRepo = employeeRepo;

        logger.info("LeaveBalanceServiceImpl initialized");
    }

    @Override
    public ApiResponse getEmployeeBalances(Long employeeId) {

        logger.info("Fetching leave balances for employeeId: {}", employeeId);

        var employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> {
                    logger.error("Employee not found with id: {}", employeeId);
                    return new EmployeeNotFoundException(
                            "Employee not found with id: " + employeeId);
                });

        List<LeaveBalanceDTO> balances =
                leaveBalanceRepo.findByEmployee_Id(employeeId)
                .stream()
                .map(balance -> new LeaveBalanceDTO(
                        balance.getId(),
                        employee.getId(),
                        employee.getFirstName(),
                        balance.getLeaveType().getId(),
                        balance.getLeaveType().getTypeName(),
                        balance.getTotalLeaves(),
                        balance.getUsedLeaves(),
                        balance.getRemainingLeaves()
                ))
                .collect(Collectors.toList());

        logger.debug("Total leave balances fetched for employee {} : {}",
                employeeId, balances.size());

        return new ApiResponse(
                200,
                "Leave balances fetched successfully",
                balances
        );
    }
}