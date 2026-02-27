package com.revworkforce.employeeservice;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.LeaveRequestDTO;

public interface LeaveRequestService {

    ApiResponse applyLeave(LeaveRequestDTO dto);

    ApiResponse cancelLeave(Long leaveId);

    ApiResponse getLeaveHistory(Long employeeId, int page, int size);
    ApiResponse getLeaveStatus(Long employeeId, int page, int size);
}
