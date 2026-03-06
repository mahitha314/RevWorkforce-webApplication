package com.revworkforce.adminserviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.adminservice.LeaveManagementService;
import com.revworkforce.dto.LeaveBalanceDTO;
import com.revworkforce.dto.LeaveTypeDTO;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveBalance;
import com.revworkforce.model.LeaveType;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.LeaveBalanceRepository;
import com.revworkforce.repository.LeaveTypeRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class LeaveManagementServiceImpl implements LeaveManagementService {

	private final LeaveTypeRepository leaveTypeRepository;
	private final LeaveBalanceRepository leaveBalanceRepository;
	private final EmployeeRepository employeeRepository;
	private final NotificationService notificationService;
	private final ActivityLogService activityLogService;
	private final HttpServletRequest request;

	public LeaveManagementServiceImpl(LeaveTypeRepository leaveTypeRepository,
			LeaveBalanceRepository leaveBalanceRepository, EmployeeRepository employeeRepository,
			NotificationService notificationService, ActivityLogService activityLogService,
			HttpServletRequest request) {

		this.leaveTypeRepository = leaveTypeRepository;
		this.leaveBalanceRepository = leaveBalanceRepository;
		this.employeeRepository = employeeRepository;
		this.notificationService = notificationService;
		this.activityLogService = activityLogService;
		this.request = request;
	}

	@Override
	public LeaveTypeDTO createLeaveType(LeaveTypeDTO dto) {

		if (leaveTypeRepository.existsByTypeName(dto.getTypeName())) {
			throw new RuntimeException("Leave type already exists.");
		}

		LeaveType leaveType = new LeaveType();
		leaveType.setTypeName(dto.getTypeName());
		leaveType.setTotalDays(dto.getTotalDays());

		LeaveType saved = leaveTypeRepository.save(leaveType);

		activityLogService.log("Created Leave Type", "Leave Management", "Created leave type: " + saved.getTypeName(),
				"SUCCESS", request);

		return new LeaveTypeDTO(saved.getId(), saved.getTypeName(), saved.getTotalDays());
	}

	@Override
	public List<LeaveTypeDTO> getAllLeaveTypes() {

		return leaveTypeRepository.findAll().stream()
				.map(type -> new LeaveTypeDTO(type.getId(), type.getTypeName(), type.getTotalDays())).toList();
	}

	@Override
	public LeaveBalance assignLeaveToEmployee(Long employeeId, Long leaveTypeId, int totalDays) {

		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found"));

		LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
				.orElseThrow(() -> new RuntimeException("Leave type not found"));

		if (leaveBalanceRepository.findByEmployeeAndLeaveType(employee, leaveType).isPresent()) {
			throw new RuntimeException("Leave already assigned to employee.");
		}

		LeaveBalance balance = new LeaveBalance();
		balance.setEmployee(employee);
		balance.setLeaveType(leaveType);
		balance.setTotalLeaves(totalDays);
		balance.setUsedLeaves(0);
		balance.setRemainingLeaves(totalDays);

		LeaveBalance saved = leaveBalanceRepository.save(balance);

		NotificationDTO notification = new NotificationDTO();
		notification.setEmployeeId(employee.getEmployeeId());
		notification.setTitle("Leave Assigned");
		notification.setMessage("You have been assigned " + totalDays + " days of " + leaveType.getTypeName());
		notification.setType("LEAVE");
		notification.setStatus("ACTIVE");

		notificationService.createNotification(notification);

		activityLogService.log("Assigned Leave", "Leave Management",
				"Assigned " + totalDays + " days of " + leaveType.getTypeName() + " to " + employee.getFirstName(),
				"SUCCESS", request);

		return saved;
	}

	@Override
	public LeaveBalance adjustLeave(LeaveBalanceDTO dto) {

		Employee employee = employeeRepository.findById(dto.getEmployeeId())
				.orElseThrow(() -> new RuntimeException("Employee not found"));

		LeaveType leaveType = leaveTypeRepository.findById(dto.getLeaveTypeId())
				.orElseThrow(() -> new RuntimeException("Leave type not found"));

		LeaveBalance balance = leaveBalanceRepository.findByEmployeeAndLeaveType(employee, leaveType)
				.orElseThrow(() -> new RuntimeException("Leave balance not found"));

		balance.setTotalLeaves(balance.getTotalLeaves() + dto.getDays());
		balance.setRemainingLeaves(balance.getRemainingLeaves() + dto.getDays());

		LeaveBalance updated = leaveBalanceRepository.save(balance);

		activityLogService.log("Adjusted Leave", "Leave Management", "Adjusted " + dto.getDays() + " days for "
				+ employee.getFirstName() + " (" + leaveType.getTypeName() + ")", "SUCCESS", request);

		return updated;

	}

	@Override
	public List<LeaveBalance> getEmployeeLeaveInfo(Long employeeId) {

		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found"));
		return leaveBalanceRepository.findByEmployee(employee);
	}

	@Override
	public List<LeaveBalance> getDepartmentLeaveReport(Long departmentId) {
		return leaveBalanceRepository.findByDepartmentId(departmentId);
	}

	@Override
	public long countLeaves() {
		return leaveTypeRepository.count();
	}
}