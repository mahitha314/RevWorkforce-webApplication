
package com.revworkforce.managerservice;

import com.revworkforce.dto.ApiResponse;

public interface TeamLeavesService {

    ApiResponse getDirectReportees(Long managerId);

    ApiResponse getTeamLeaveRequests(Long managerId);

    ApiResponse approveLeave(Long managerId, Long leaveId, String comments);

    ApiResponse rejectLeave(Long managerId, Long leaveId, String comments);

    ApiResponse getTeamLeaveCalendar(Long managerId);

    ApiResponse getTeamLeaveBalance(Long managerId);
    
}
