package com.revworkforce.adminservice;

import com.revworkforce.dto.AdjustLeaveDTO;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.LeaveBalanceDTO;
import com.revworkforce.dto.LeaveTypeDTO;

public interface LeaveManagementService {

	ApiResponse createLeaveType(LeaveTypeDTO dto);

	ApiResponse getAllLeaveTypes();

	ApiResponse assignLeave(LeaveBalanceDTO dto);

	ApiResponse adjustLeave(AdjustLeaveDTO dto);

	ApiResponse getAllEmployeeLeaves();

	ApiResponse getEmployeeLeave(Long empId);

	ApiResponse getDepartmentReport(Long deptId);

}