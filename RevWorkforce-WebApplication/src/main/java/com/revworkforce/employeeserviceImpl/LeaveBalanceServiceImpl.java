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

	public LeaveBalanceServiceImpl(LeaveBalanceRepository leaveBalanceRepo, EmployeeRepository employeeRepo) {

		this.leaveBalanceRepo = leaveBalanceRepo;
		this.employeeRepo = employeeRepo;
	}

	@Override
	public ApiResponse getEmployeeBalances(Long employeeId) {

		var employee = employeeRepo.findById(employeeId)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

		List<LeaveBalanceDTO> balances = leaveBalanceRepo.findByEmployee_Id(employeeId).stream()
				.map(balance -> new LeaveBalanceDTO(balance.getId(), employee.getId(), employee.getFirstName(),
						balance.getLeaveType().getId(), balance.getLeaveType().getTypeName(), balance.getTotalLeaves(),
						balance.getUsedLeaves(), balance.getRemainingLeaves()))
				.collect(Collectors.toList());

		return new ApiResponse(200, "Leave balances fetched successfully", balances);
	}
}