package com.revworkforce.adminservice;

import java.util.List;

import com.revworkforce.dto.LeaveBalanceDTO;
import com.revworkforce.dto.LeaveTypeDTO;
import com.revworkforce.model.LeaveBalance;
import com.revworkforce.model.LeaveType;

public interface LeaveManagementService {
	 LeaveTypeDTO createLeaveType(LeaveTypeDTO dto);
	    List<LeaveTypeDTO> getAllLeaveTypes();

	    // Assign Leave
	    LeaveBalance assignLeaveToEmployee(Long employeeId,
	                                       Long leaveTypeId,
	                                       int totalDays);

	    // Adjust Leave
	    LeaveBalance adjustLeave(LeaveBalanceDTO dto);

	    // Reports
	    List<LeaveBalance> getEmployeeLeaveInfo(Long employeeId);
	    List<LeaveBalance> getDepartmentLeaveReport(Long departmentId);
	    long countLeaves();
}