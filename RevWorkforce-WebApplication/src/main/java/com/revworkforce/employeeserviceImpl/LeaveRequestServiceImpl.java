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

import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

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
    }

    
    @Override
    public ApiResponse applyLeave(LeaveRequestDTO dto) {

        Employee employee = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LeaveType leaveType = leaveTypeRepo.findById(dto.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("Leave type not found"));

        LeaveRequest request = new LeaveRequest();
        request.setEmployee(employee);
        request.setLeaveType(leaveType);
        request.setStartDate(dto.getStartDate());
        request.setEndDate(dto.getEndDate());
        request.setReason(dto.getReason());

       
        LeaveApproval approval = new LeaveApproval();
        approval.setStatus("PENDING");
        approval.setLeaveRequest(request);

        request.setLeaveApproval(approval);

        leaveRequestRepo.save(request);

        NotificationDTO notification = new NotificationDTO();
        notification.setTitle("New Leave Request");
        notification.setMessage(employee.getFirstName()
                + " applied for leave from "
                + dto.getStartDate() + " to " + dto.getEndDate());
        notification.setType("LEAVE");
        notification.setStatus("ACTIVE");

        notificationService.createNotificationForAll(notification); // change if manager-specific

        activityLogService.log(
                "LEAVE_APPLIED",
                "Leave Management",
                "Employee " + employee.getFirstName()
                        + " applied leave",
                "SUCCESS",
                request
                
        );

        return new ApiResponse(201,
                "Leave Applied Successfully",
                null);
    }

    @Override
    public ApiResponse cancelLeave(Long leaveId) {

        LeaveRequest request = leaveRequestRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        if (request.getLeaveApproval() != null &&
                !request.getLeaveApproval().getStatus().equalsIgnoreCase("PENDING")) {

            return new ApiResponse(400,
                    "Only Pending Leave Can Be Cancelled",
                    null);
        }

        if (request.getLeaveApproval() != null) {
            request.getLeaveApproval().setStatus("CANCELLED");
        }

        leaveRequestRepo.save(request);

        NotificationDTO notification = new NotificationDTO();
        notification.setTitle("Leave Cancelled");
        notification.setMessage(request.getEmployee().getFirstName()
                + " cancelled their leave request.");
        notification.setType("LEAVE");
        notification.setStatus("ACTIVE");

        notificationService.createNotificationForAll(notification);

        activityLogService.log(
                "LEAVE_CANCELLED",
                "Leave Management",
                "Leave cancelled by "
                        + request.getEmployee().getFirstName(),
                "SUCCESS",
                request
        );

        return new ApiResponse(200,
                "Leave Cancelled Successfully",
                null);
    }

   
    @Override
    public ApiResponse getLeaveHistory(Long employeeId) {

    	List<LeaveRequest> leaves =
                leaveRequestRepo.findByEmployee_IdOrderByStartDateDesc(employeeId);

        List<LeaveRequestDTO> dtoList =
                leaves.stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());

        return new ApiResponse(200,
                "Leave History Fetched Successfully",
                dtoList);
    }

    
    @Override
    public ApiResponse getPendingLeaves(Long employeeId) {

        List<LeaveRequest> leaves =
                leaveRequestRepo.findByEmployee_Id(employeeId);

        List<LeaveRequestDTO> dtoList =
                leaves.stream()
                        .map(this::convertToDTO)
                        .filter(dto -> dto.getStatus().equalsIgnoreCase("PENDING"))
                        .collect(Collectors.toList());

        return new ApiResponse(200,
                "Pending Leaves Fetched Successfully",
                dtoList);
    }

   
    @Override
    public ApiResponse getLeaveById(Long leaveId) {

        LeaveRequest leave = leaveRequestRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

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
                        ? leave.getEmployee().getFirstName() + " " +
                          leave.getEmployee().getLastName()
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