package com.revworkforce.employeeserviceImpl;

import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.LeaveRequestDTO;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.employeeservice.LeaveRequestService;
import com.revworkforce.model.*;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.*;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private static final Logger logger =
            LoggerFactory.getLogger(LeaveRequestServiceImpl.class);

    private final LeaveRequestRepository leaveRequestRepo;
    private final EmployeeRepository employeeRepo;
    private final LeaveTypeRepository leaveTypeRepo;
    private final NotificationService notificationService;
    private final ActivityLogService activityLogService;
    private final HttpServletRequest request;

    public LeaveRequestServiceImpl(
            LeaveRequestRepository leaveRequestRepo,
            EmployeeRepository employeeRepo,
            LeaveTypeRepository leaveTypeRepo,
            NotificationService notificationService,
            ActivityLogService activityLogService,
            HttpServletRequest request) {

        this.leaveRequestRepo = leaveRequestRepo;
        this.employeeRepo = employeeRepo;
        this.leaveTypeRepo = leaveTypeRepo;
        this.notificationService = notificationService;
        this.activityLogService = activityLogService;
        this.request = request;

        logger.info("LeaveRequestServiceImpl initialized");
    }

    @Override
    public ApiResponse applyLeave(LeaveRequestDTO dto) {

        logger.info("Employee {} applying leave", dto.getEmployeeId());

        Employee employee = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> {
                    logger.error("Employee not found with id {}", dto.getEmployeeId());
                    return new RuntimeException("Employee not found");
                });

        LeaveType leaveType = leaveTypeRepo.findById(dto.getLeaveTypeId())
                .orElseThrow(() -> {
                    logger.error("Leave type not found with id {}", dto.getLeaveTypeId());
                    return new RuntimeException("Leave type not found");
                });

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(leaveType);
        leaveRequest.setStartDate(dto.getStartDate());
        leaveRequest.setEndDate(dto.getEndDate());
        leaveRequest.setReason(dto.getReason());

        LeaveApproval approval = new LeaveApproval();
        approval.setStatus("PENDING");
        approval.setLeaveRequest(leaveRequest);

        leaveRequest.setLeaveApproval(approval);

        leaveRequestRepo.save(leaveRequest);

        logger.debug("Leave request saved for employee {}", employee.getEmployeeId());

        NotificationDTO notification = new NotificationDTO();
        notification.setTitle("New Leave Request");
        notification.setMessage(employee.getFirstName()
                + " applied for leave from "
                + dto.getStartDate() + " to " + dto.getEndDate());
        notification.setType("LEAVE");
        notification.setStatus("ACTIVE");

        notificationService.createNotificationForAll(notification);

        logger.info("Notification sent for leave request");

        activityLogService.log(
                "LEAVE_APPLIED",
                "Leave Management",
                "Employee " + employee.getFirstName() + " applied leave",
                "SUCCESS",
                request
        );

        return new ApiResponse(201,
                "Leave Applied Successfully",
                null);
    }

    @Override
    public ApiResponse cancelLeave(Long leaveId) {

        logger.info("Cancelling leave request {}", leaveId);

        LeaveRequest leaveRequest = leaveRequestRepo.findById(leaveId)
                .orElseThrow(() -> {
                    logger.error("Leave request not found with id {}", leaveId);
                    return new RuntimeException("Leave not found");
                });

        if (leaveRequest.getLeaveApproval() != null &&
                !leaveRequest.getLeaveApproval().getStatus().equalsIgnoreCase("PENDING")) {

            logger.warn("Attempt to cancel non-pending leave {}", leaveId);

            return new ApiResponse(400,
                    "Only Pending Leave Can Be Cancelled",
                    null);
        }

        if (leaveRequest.getLeaveApproval() != null) {
            leaveRequest.getLeaveApproval().setStatus("CANCELLED");
        }

        leaveRequestRepo.save(leaveRequest);

        logger.debug("Leave request {} cancelled", leaveId);

        NotificationDTO notification = new NotificationDTO();
        notification.setTitle("Leave Cancelled");
        notification.setMessage(leaveRequest.getEmployee().getFirstName()
                + " cancelled their leave request.");
        notification.setType("LEAVE");
        notification.setStatus("ACTIVE");

        notificationService.createNotificationForAll(notification);

        activityLogService.log(
                "LEAVE_CANCELLED",
                "Leave Management",
                "Leave cancelled by " + leaveRequest.getEmployee().getFirstName(),
                "SUCCESS",
                request
        );

        return new ApiResponse(200,
                "Leave Cancelled Successfully",
                null);
    }

    @Override
    public ApiResponse getLeaveHistory(Long employeeId) {

        logger.info("Fetching leave history for employee {}", employeeId);

        List<LeaveRequest> leaves =
                leaveRequestRepo.findByEmployee_IdOrderByStartDateDesc(employeeId);

        List<LeaveRequestDTO> dtoList =
                leaves.stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());

        logger.debug("Total leave history records: {}", dtoList.size());

        return new ApiResponse(200,
                "Leave History Fetched Successfully",
                dtoList);
    }

    @Override
    public ApiResponse getPendingLeaves(Long employeeId) {

        logger.info("Fetching pending leaves for employee {}", employeeId);

        List<LeaveRequest> leaves =
                leaveRequestRepo.findByEmployee_Id(employeeId);

        List<LeaveRequestDTO> dtoList =
                leaves.stream()
                        .map(this::convertToDTO)
                        .filter(dto -> dto.getStatus().equalsIgnoreCase("PENDING"))
                        .collect(Collectors.toList());

        logger.debug("Pending leaves count: {}", dtoList.size());

        return new ApiResponse(200,
                "Pending Leaves Fetched Successfully",
                dtoList);
    }

    @Override
    public ApiResponse getLeaveById(Long leaveId) {

        logger.info("Fetching leave details for id {}", leaveId);

        LeaveRequest leave = leaveRequestRepo.findById(leaveId)
                .orElseThrow(() -> {
                    logger.error("Leave not found with id {}", leaveId);
                    return new RuntimeException("Leave not found");
                });

        LeaveRequestDTO dto = convertToDTO(leave);

        return new ApiResponse(200,
                "Leave Details Fetched Successfully",
                dto);
    }

    private LeaveRequestDTO convertToDTO(LeaveRequest leave) {

        long days = ChronoUnit.DAYS.between(
                leave.getStartDate(),
                leave.getEndDate()) + 1;

        String status = "PENDING";

        if (leave.getLeaveApproval() != null) {
            status = leave.getLeaveApproval().getStatus() != null
                    ? leave.getLeaveApproval().getStatus()
                    : "PENDING";
        }

        return new LeaveRequestDTO(
                leave.getId(),
                leave.getEmployee() != null ? leave.getEmployee().getId() : null,
                leave.getEmployee() != null
                        ? leave.getEmployee().getFirstName() + " "
                        + leave.getEmployee().getLastName()
                        : "N/A",
                leave.getLeaveType() != null ? leave.getLeaveType().getId() : null,
                leave.getLeaveType() != null
                        ? leave.getLeaveType().getTypeName()
                        : "N/A",
                leave.getStartDate(),
                leave.getEndDate(),
                (int) days,
                leave.getReason(),
                status
        );
    }
}