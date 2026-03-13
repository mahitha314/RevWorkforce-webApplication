package com.revworkforce.managerserviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.exception.ResourceNotFoundException;
import com.revworkforce.managerservice.TeamLeavesService;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveApproval;
import com.revworkforce.model.LeaveBalance;
import com.revworkforce.model.LeaveRequest;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.LeaveApprovalRepository;
import com.revworkforce.repository.LeaveBalanceRepository;
import com.revworkforce.repository.LeaveRequestRepository;
import jakarta.transaction.Transactional;

@Service
public class TeamLeavesServiceImpl implements TeamLeavesService {

	private static final Logger logger = LogManager.getLogger(TeamLeavesServiceImpl.class);

	private final EmployeeRepository employeeRepo;
	private final LeaveRequestRepository leaveRequestRepo;
	private final LeaveApprovalRepository approvalRepo;
	private final LeaveBalanceRepository leaveBalanceRepo;
	private final NotificationService notificationService;
	private final ActivityLogService activityLogService;

	public TeamLeavesServiceImpl(EmployeeRepository employeeRepo, LeaveRequestRepository leaveRequestRepo,
			LeaveApprovalRepository approvalRepo, LeaveBalanceRepository leaveBalanceRepo,
			NotificationService notificationService, ActivityLogService activityLogService) {

		this.employeeRepo = employeeRepo;
		this.leaveRequestRepo = leaveRequestRepo;
		this.approvalRepo = approvalRepo;
		this.leaveBalanceRepo = leaveBalanceRepo;
		this.notificationService = notificationService;
		this.activityLogService = activityLogService;
	}

	private Employee validateManager(Long managerId) {

		logger.debug("Validating manager with id {}", managerId);

		return employeeRepo.findById(managerId).orElseThrow(() -> {
			logger.error("Manager not found with id {}", managerId);
			return new ResourceNotFoundException("Manager not found");
		});
	}

	@Override
	@Transactional
	public ApiResponse getDirectReportees(Long managerId) {

		logger.info("Fetching direct reportees for manager {}", managerId);

		if (!employeeRepo.existsById(managerId)) {

			logger.warn("Manager not found {}", managerId);
			return new ApiResponse(404, "Manager not found", null);
		}

		List<Employee> team = employeeRepo.findByManager_Id(managerId);

		logger.debug("Total reportees found: {}", team.size());

		return new ApiResponse(200, "Direct reportees fetched", team);
	}

	@Override
	public ApiResponse getTeamLeaveRequests(Long managerId) {

		logger.info("Fetching team leave requests for manager {}", managerId);

		if (!employeeRepo.existsById(managerId)) {

			logger.warn("Manager not found {}", managerId);
			return new ApiResponse(404, "Manager not found", null);
		}

		List<LeaveRequest> requests = leaveRequestRepo.findByEmployee_Manager_Id(managerId);

		logger.debug("Total leave requests found {}", requests.size());

		activityLogService.log(managerId, "Viewed team leave requests");

		return new ApiResponse(200, "Team leave requests fetched", requests);
	}

	@Override
	@Transactional
	public ApiResponse approveLeave(Long managerId, Long leaveId, String comments) {

		logger.info("Manager {} approving leave {}", managerId, leaveId);

		LeaveRequest request = leaveRequestRepo.findById(leaveId).orElse(null);

		if (request == null) {

			logger.error("Leave request not found {}", leaveId);
			return new ApiResponse(404, "Leave not found", null);
		}

		if (request.getLeaveApproval() != null) {

			logger.warn("Leave already processed {}", leaveId);
			return new ApiResponse(400, "Leave already processed", null);
		}

		if (request.getEmployee().getManager() == null
				|| !request.getEmployee().getManager().getId().equals(managerId)) {

			logger.warn("Unauthorized leave approval attempt by manager {}", managerId);
			return new ApiResponse(403, "Unauthorized to approve this leave", null);
		}

		Employee manager = employeeRepo.findById(managerId).orElse(null);

		LeaveApproval approval = new LeaveApproval();
		approval.setManager(manager);
		approval.setStatus("APPROVED");
		approval.setComments(comments);
		approval.setApprovalDate(LocalDate.now());

		approvalRepo.save(approval);

		request.setLeaveApproval(approval);
		leaveRequestRepo.save(request);

		LeaveBalance balance = leaveBalanceRepo
				.findByEmployee_IdAndLeaveType_Id(request.getEmployee().getId(), request.getLeaveType().getId())
				.orElseGet(() -> {

					logger.debug("Creating new leave balance record");

					LeaveBalance newBalance = new LeaveBalance();
					newBalance.setEmployee(request.getEmployee());
					newBalance.setLeaveType(request.getLeaveType());
					newBalance.setTotalLeaves(10);
					newBalance.setUsedLeaves(0);
					newBalance.setRemainingLeaves(10);

					return leaveBalanceRepo.save(newBalance);
				});

		long days = java.time.temporal.ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;

		if (balance.getRemainingLeaves() < days) {

			logger.warn("Insufficient leave balance for employee {}", request.getEmployee().getId());

			return new ApiResponse(400, "Insufficient leave balance", null);
		}

		balance.setUsedLeaves(balance.getUsedLeaves() + (int) days);
		balance.setRemainingLeaves(balance.getRemainingLeaves() - (int) days);

		leaveBalanceRepo.save(balance);

		NotificationDTO notification = new NotificationDTO();
		notification.setEmployeeId(request.getEmployee().getEmployeeId());
		notification.setTitle("Leave Approved");
		notification.setMessage("Your leave request has been approved.");
		notification.setType("LEAVE");
		notification.setStatus("APPROVED");
		notification.setIsRead(false);
		notification.setCreatedAt(LocalDateTime.now());
		notification.setReferenceId(request.getId());

		notificationService.createNotification(notification);

		logger.info("Leave {} approved successfully for employee {}", leaveId, request.getEmployee().getEmployeeId());

		activityLogService.log(managerId,
				"Approved leave ID " + leaveId + " for employee ID " + request.getEmployee().getId());

		return new ApiResponse(200, "Leave approved successfully", request);
	}

	@Override
	@Transactional
	public ApiResponse rejectLeave(Long managerId, Long leaveId, String comments) {

		logger.info("Manager {} rejecting leave {}", managerId, leaveId);

		if (comments == null || comments.isBlank()) {

			logger.warn("Rejection comments missing for leave {}", leaveId);
			return new ApiResponse(400, "Comments mandatory for rejection", null);
		}

		LeaveRequest request = leaveRequestRepo.findById(leaveId).orElse(null);

		if (request == null) {

			logger.error("Leave request not found {}", leaveId);
			return new ApiResponse(404, "Leave not found", null);
		}

		if (request.getLeaveApproval() != null) {

			logger.warn("Leave already processed {}", leaveId);
			return new ApiResponse(400, "Leave already processed", null);
		}

		if (request.getEmployee().getManager() == null
				|| !request.getEmployee().getManager().getId().equals(managerId)) {

			logger.warn("Unauthorized rejection attempt by manager {}", managerId);
			return new ApiResponse(403, "Unauthorized to reject this leave", null);
		}

		Employee manager = employeeRepo.findById(managerId).orElse(null);

		LeaveApproval approval = new LeaveApproval();
		approval.setManager(manager);
		approval.setStatus("REJECTED");
		approval.setComments(comments);
		approval.setApprovalDate(LocalDate.now());

		approvalRepo.save(approval);

		request.setLeaveApproval(approval);
		leaveRequestRepo.save(request);

		NotificationDTO notification = new NotificationDTO();
		notification.setEmployeeId(request.getEmployee().getEmployeeId());
		notification.setTitle("Leave Rejected");
		notification.setMessage("Your leave was rejected. Reason: " + comments);
		notification.setType("LEAVE");
		notification.setStatus("REJECTED");
		notification.setIsRead(false);
		notification.setCreatedAt(LocalDateTime.now());
		notification.setReferenceId(request.getId());

		notificationService.createNotification(notification);

		logger.info("Leave {} rejected for employee {}", leaveId, request.getEmployee().getEmployeeId());

		activityLogService.log(managerId,
				"Rejected leave ID " + leaveId + " for employee ID " + request.getEmployee().getId());

		return new ApiResponse(200, "Leave rejected successfully", request);
	}

	@Override
	public ApiResponse getTeamLeaveCalendar(Long managerId) {

		logger.info("Fetching leave calendar for manager {}", managerId);

		if (!employeeRepo.existsById(managerId)) {

			logger.warn("Manager not found {}", managerId);
			return new ApiResponse(404, "Manager not found", null);
		}

		List<LeaveRequest> calendar = leaveRequestRepo.findByEmployee_Manager_Id(managerId);

		return new ApiResponse(200, "Team leave calendar fetched", calendar);
	}

	@Override
	public ApiResponse getTeamLeaveBalance(Long managerId) {

		logger.info("Fetching team leave balances for manager {}", managerId);

		if (!employeeRepo.existsById(managerId)) {

			logger.warn("Manager not found {}", managerId);
			return new ApiResponse(404, "Manager not found", null);
		}

		List<Employee> team = employeeRepo.findByManager_Id(managerId);

		Map<String, Object> result = new HashMap<>();

		for (Employee emp : team) {

			result.put(emp.getFirstName() + " " + emp.getLastName(), leaveBalanceRepo.findByEmployee_Id(emp.getId()));
		}

		activityLogService.log(managerId, "Viewed team leave balances");

		logger.info("Leave balances fetched successfully for manager {}", managerId);

		return new ApiResponse(200, "Team leave balance fetched", result);
	}

}