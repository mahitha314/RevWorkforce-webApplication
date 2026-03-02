package com.revworkforce.employeeservice;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.LeaveRequestDTO;

public interface LeaveRequestService {

    // ================= APPLY LEAVE =================
    ApiResponse applyLeave(LeaveRequestDTO dto);

    // ================= CANCEL LEAVE =================
    ApiResponse cancelLeave(Long leaveId);

    // ================= GET ALL LEAVE HISTORY (PAGINATED) =================
    ApiResponse getLeaveHistory(Long employeeId);

    // ================= GET ONLY PENDING LEAVES (PAGINATED) =================
    ApiResponse getPendingLeaves(Long employeeId);

    // ================= GET SINGLE LEAVE DETAILS =================
    ApiResponse getLeaveById(Long leaveId);

}