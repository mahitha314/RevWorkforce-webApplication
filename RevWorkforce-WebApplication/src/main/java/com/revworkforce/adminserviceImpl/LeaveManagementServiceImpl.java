package com.revworkforce.adminserviceImpl;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private static final Logger logger = LogManager.getLogger(LeaveManagementServiceImpl.class);

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

		logger.info("Creating leave type {}", dto.getTypeName());

		if (leaveTypeRepository.existsByTypeName(dto.getTypeName())) {
			logger.warn("Leave type already exists {}", dto.getTypeName());
			throw new RuntimeException("Leave type already exists.");
		}

		LeaveType leaveType = new LeaveType();
		leaveType.setTypeName(dto.getTypeName());
		leaveType.setTotalDays(dto.getTotalDays());

		LeaveType saved = leaveTypeRepository.save(leaveType);

		logger.info("Leave type created successfully {}", saved.getTypeName());

		activityLogService.log("Created Leave Type", "Leave Management", "Created leave type: " + saved.getTypeName(),
				"SUCCESS", request);

		return new LeaveTypeDTO(saved.getId(), saved.getTypeName(), saved.getTotalDays());
	}

	@Override
	public List<LeaveTypeDTO> getAllLeaveTypes() {

		logger.info("Fetching all leave types");

		List<LeaveTypeDTO> types = leaveTypeRepository.findAll().stream()
				.map(type -> new LeaveTypeDTO(type.getId(), type.getTypeName(), type.getTotalDays())).toList();

		logger.debug("Total leave types fetched {}", types.size());

		return types;
	}

	@Override
	public LeaveBalance assignLeaveToEmployee(Long employeeId, Long leaveTypeId, int totalDays) {

		logger.info("Assigning leave {} days for employee {}", totalDays, employeeId);

		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found"));

		LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
				.orElseThrow(() -> new RuntimeException("Leave type not found"));

		if (leaveBalanceRepository.findByEmployeeAndLeaveType(employee, leaveType).isPresent()) {
			logger.warn("Leave already assigned for employee {}", employeeId);
			throw new RuntimeException("Leave already assigned to employee.");
		}

		LeaveBalance balance = new LeaveBalance();
		balance.setEmployee(employee);
		balance.setLeaveType(leaveType);
		balance.setTotalLeaves(totalDays);
		balance.setUsedLeaves(0);
		balance.setRemainingLeaves(totalDays);

		LeaveBalance saved = leaveBalanceRepository.save(balance);

		logger.info("Leave assigned successfully to {}", employee.getFirstName());

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

		logger.info("Adjusting leave for employee {}", dto.getEmployeeId());

		Employee employee = employeeRepository.findById(dto.getEmployeeId())
				.orElseThrow(() -> new RuntimeException("Employee not found"));

		LeaveType leaveType = leaveTypeRepository.findById(dto.getLeaveTypeId())
				.orElseThrow(() -> new RuntimeException("Leave type not found"));

		LeaveBalance balance = leaveBalanceRepository.findByEmployeeAndLeaveType(employee, leaveType)
				.orElseThrow(() -> new RuntimeException("Leave balance not found"));

		balance.setTotalLeaves(balance.getTotalLeaves() + dto.getDays());
		balance.setRemainingLeaves(balance.getRemainingLeaves() + dto.getDays());

		LeaveBalance updated = leaveBalanceRepository.save(balance);

		logger.info("Leave adjusted successfully for {}", employee.getFirstName());

		activityLogService.log("Adjusted Leave", "Leave Management", "Adjusted " + dto.getDays() + " days for "
				+ employee.getFirstName() + " (" + leaveType.getTypeName() + ")", "SUCCESS", request);

		return updated;
	}

	@Override
	public List<LeaveBalance> getEmployeeLeaveInfo(Long employeeId) {

		logger.info("Fetching leave info for employee {}", employeeId);

		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found"));

		return leaveBalanceRepository.findByEmployee(employee);
	}

	@Override
	public List<LeaveBalance> getDepartmentLeaveReport(Long departmentId) {

		logger.info("Fetching leave report for department {}", departmentId);

		return leaveBalanceRepository.findByDepartmentId(departmentId);
	}

	@Override
	public long countLeaves() {

		logger.debug("Counting total leave types");

		return leaveTypeRepository.count();
	}

	@Override
	public List<Employee> getEmployeesNotAssignedToLeaveType(Long leaveTypeId) {

		logger.info("Fetching employees not assigned to leave type {}", leaveTypeId);

		LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
				.orElseThrow(() -> new RuntimeException("Leave type not found"));

		return employeeRepository.findEmployeesNotAssignedToLeaveType(leaveType.getId());
	}

}