package com.revworkforce.adminservice;

import java.util.List;
import com.revworkforce.dto.LeaveBalanceDTO;
import com.revworkforce.dto.LeaveTypeDTO;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveBalance;

public interface LeaveManagementService {

	LeaveTypeDTO createLeaveType(LeaveTypeDTO dto);

	List<LeaveTypeDTO> getAllLeaveTypes();

	LeaveBalance assignLeaveToEmployee(Long employeeId, Long leaveTypeId, int totalDays);

	LeaveBalance adjustLeave(LeaveBalanceDTO dto);

	List<LeaveBalance> getEmployeeLeaveInfo(Long employeeId);

	List<LeaveBalance> getDepartmentLeaveReport(Long departmentId);

	long countLeaves();

	List<Employee> getEmployeesNotAssignedToLeaveType(Long leaveTypeId);

}