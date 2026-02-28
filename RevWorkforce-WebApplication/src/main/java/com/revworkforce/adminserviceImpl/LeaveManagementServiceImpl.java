package com.revworkforce.adminserviceImpl;

import com.revworkforce.adminservice.LeaveManagementService;
import com.revworkforce.dto.AdjustLeaveDTO;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.LeaveBalanceDTO;
import com.revworkforce.dto.LeaveTypeDTO;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.LeaveBalanceRepository;
import com.revworkforce.repository.LeaveTypeRepository;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveBalance;
import com.revworkforce.model.LeaveType;
import com.revworkforce.notification.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class LeaveManagementServiceImpl implements LeaveManagementService {

	private final LeaveTypeRepository leaveTypeRepository;
	private final LeaveBalanceRepository leaveBalanceRepository;
	private final EmployeeRepository employeeRepository;
	private final NotificationService notificationService;

	public LeaveManagementServiceImpl(LeaveTypeRepository leaveTypeRepository,
			LeaveBalanceRepository leaveBalanceRepository, EmployeeRepository employeeRepository,
			NotificationService notificationService) {

		this.leaveTypeRepository = leaveTypeRepository;
		this.leaveBalanceRepository = leaveBalanceRepository;
		this.employeeRepository = employeeRepository;
		this.notificationService = notificationService;
	}

	@Override
	public ApiResponse createLeaveType(LeaveTypeDTO dto) {

		if (leaveTypeRepository.existsByTypeName(dto.getTypeName())) {
			return new ApiResponse(400, "Leave type already exists", null);
		}

		LeaveType leaveType = new LeaveType();
		leaveType.setTypeName(dto.getTypeName());
		leaveType.setTotalDays(dto.getTotalDays());

		leaveTypeRepository.save(leaveType);

		return new ApiResponse(201, "Leave type created successfully", leaveType);
	}

	@Override
	public ApiResponse getAllLeaveTypes() {
		return new ApiResponse(200, "Leave types fetched", leaveTypeRepository.findAll());
	}

	@Override
	public ApiResponse assignLeave(LeaveBalanceDTO dto) {

		Employee employee = employeeRepository.findById(dto.getEmployeeId()).orElse(null);

		LeaveType leaveType = leaveTypeRepository.findById(dto.getLeaveTypeId()).orElse(null);

		if (employee == null || leaveType == null) {
			return new ApiResponse(404, "Employee or LeaveType not found", null);
		}

		LeaveBalance balance = new LeaveBalance();
		balance.setEmployee(employee);
		balance.setLeaveType(leaveType);
		balance.setTotalLeaves(dto.getTotalLeaves());
		balance.setUsedLeaves(0);
		balance.setRemainingLeaves(dto.getTotalLeaves());

		leaveBalanceRepository.save(balance);

		NotificationDTO notification = new NotificationDTO();
		notification.setEmployeeId(employee.getEmployeeId());
		notification.setTitle("Leave Assigned");
		notification.setMessage(
				"Admin assigned " + dto.getTotalLeaves() + " " + leaveType.getTypeName() + " leaves to you.");
		notification.setType("LEAVE");
		notification.setStatus("ACTIVE");
		notification.setReferenceId(balance.getId());

		notificationService.createNotification(notification);

		return new ApiResponse(201, "Leave assigned successfully", balance);
	}

	@Override
	public ApiResponse adjustLeave(AdjustLeaveDTO dto) {

		LeaveBalance balance = leaveBalanceRepository.findById(dto.getId()).orElse(null);

		if (balance == null) {
			return new ApiResponse(404, "Leave balance not found", null);
		}

		if (dto.getRemainingLeaves() < 0) {
			return new ApiResponse(400, "Invalid leave adjustment", null);
		}

		balance.setRemainingLeaves(dto.getRemainingLeaves());
		leaveBalanceRepository.save(balance);

		NotificationDTO notification = new NotificationDTO();
		notification.setEmployeeId(balance.getEmployee().getEmployeeId());
		notification.setTitle("Leave Balance Updated");
		notification.setMessage("Admin adjusted your leave balance. " + "New Remaining Leaves: "
				+ dto.getRemainingLeaves() + ". Reason: " + dto.getReason());
		notification.setType("LEAVE");
		notification.setStatus("ACTIVE");
		notification.setReferenceId(balance.getId());

		notificationService.createNotification(notification);

		return new ApiResponse(200, "Leave adjusted successfully. Reason: " + dto.getReason(), balance);
	}

	@Override
	public ApiResponse getAllEmployeeLeaves() {
		return new ApiResponse(200, "All employee leave details", leaveBalanceRepository.findAll());
	}

	@Override
	public ApiResponse getEmployeeLeave(Long empId) {
		return new ApiResponse(200, "Employee leave details", leaveBalanceRepository.findByEmployeeId(empId));
	}

	@Override
	public ApiResponse getDepartmentReport(Long deptId) {
		return new ApiResponse(200, "Department leave report", leaveBalanceRepository.findByDepartmentId(deptId));
	}

}