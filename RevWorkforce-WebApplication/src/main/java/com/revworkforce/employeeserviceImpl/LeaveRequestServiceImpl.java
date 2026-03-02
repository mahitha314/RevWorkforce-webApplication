package com.revworkforce.employeeserviceImpl;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.LeaveRequestDTO;
import com.revworkforce.employeeservice.LeaveRequestService;
import com.revworkforce.model.*;
import com.revworkforce.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private LeaveTypeRepository leaveTypeRepo;

    // ================= APPLY LEAVE =================
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

        // 🔥 Create Approval with PENDING
        LeaveApproval approval = new LeaveApproval();
        approval.setStatus("PENDING");
        approval.setLeaveRequest(request);

        request.setLeaveApproval(approval);

        leaveRequestRepo.save(request);

        return new ApiResponse(201, "Leave Applied Successfully", null);
    }

    // ================= CANCEL LEAVE =================
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

        return new ApiResponse(200,
                "Leave Cancelled Successfully",
                null);
    }

    // ================= LEAVE HISTORY =================
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

    // ================= GET ONLY PENDING =================
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

    // ================= GET SINGLE LEAVE =================
    @Override
    public ApiResponse getLeaveById(Long leaveId) {

        LeaveRequest leave = leaveRequestRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        LeaveRequestDTO dto = convertToDTO(leave);

        return new ApiResponse(200,
                "Leave Details Fetched Successfully",
                dto);
    }

    // ================= ENTITY → DTO =================
    private LeaveRequestDTO convertToDTO(LeaveRequest leave) {

        long days = ChronoUnit.DAYS.between(
                leave.getStartDate(),
                leave.getEndDate()) + 1;

        String status = "PENDING";

        if (leave.getLeaveApproval() != null &&
                leave.getLeaveApproval().getStatus() != null) {

            status = leave.getLeaveApproval().getStatus();
        }

        return new LeaveRequestDTO(
                leave.getId(),
                leave.getEmployee().getId(),
                leave.getEmployee().getFirstName() + " " +
                        leave.getEmployee().getLastName(),
                leave.getLeaveType().getId(),
                leave.getLeaveType().getTypeName(),   // change if field name differs
                leave.getStartDate(),
                leave.getEndDate(),
                (int) days,
                leave.getReason(),
                status
        );
    }
}