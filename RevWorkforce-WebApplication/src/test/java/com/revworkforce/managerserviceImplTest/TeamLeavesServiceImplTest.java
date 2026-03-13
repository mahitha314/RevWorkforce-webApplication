package com.revworkforce.managerserviceImplTest;

import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.managerserviceImpl.TeamLeavesServiceImpl;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveApproval;
import com.revworkforce.model.LeaveRequest;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamLeavesServiceImplTest {

	@Mock
	private EmployeeRepository employeeRepo;

	@Mock
	private LeaveRequestRepository leaveRequestRepo;

	@Mock
	private LeaveApprovalRepository approvalRepo;

	@Mock
	private LeaveBalanceRepository leaveBalanceRepo;

	@Mock
	private NotificationService notificationService;

	@Mock
	private ActivityLogService activityLogService;

	@InjectMocks
	private TeamLeavesServiceImpl service;

	private Employee manager;
	private Employee employee;
	private LeaveRequest leaveRequest;

	@BeforeEach
	void setup() {

		manager = new Employee();
		manager.setId(1L);

		employee = new Employee();
		employee.setId(2L);
		employee.setEmployeeId("EMP001");
		employee.setManager(manager);

		leaveRequest = new LeaveRequest();
		leaveRequest.setId(100L);
		leaveRequest.setEmployee(employee);
	}

	@Test
	void getDirectReportees_managerNotFound() {
		when(employeeRepo.existsById(1L)).thenReturn(false);

		ApiResponse response = service.getDirectReportees(1L);

		assertEquals(404, response.getStatus());
	}

	@Test
	void getDirectReportees_success() {
		when(employeeRepo.existsById(1L)).thenReturn(true);
		when(employeeRepo.findByManager_Id(1L)).thenReturn(Collections.singletonList(employee));

		ApiResponse response = service.getDirectReportees(1L);

		assertEquals(200, response.getStatus());
	}

	@Test
	void approveLeave_leaveNotFound() {

		when(leaveRequestRepo.findById(100L)).thenReturn(Optional.empty());

		ApiResponse response = service.approveLeave(1L, 100L, "Approved");

		assertEquals(404, response.getStatus());
	}

	@Test
	void approveLeave_alreadyProcessed() {

		leaveRequest.setLeaveApproval(new LeaveApproval());

		when(leaveRequestRepo.findById(100L)).thenReturn(Optional.of(leaveRequest));

		ApiResponse response = service.approveLeave(1L, 100L, "Approved");

		assertEquals(400, response.getStatus());
	}

	@Test
	void approveLeave_unauthorized() {

		Employee otherManager = new Employee();
		otherManager.setId(99L);
		employee.setManager(otherManager);

		when(leaveRequestRepo.findById(100L)).thenReturn(Optional.of(leaveRequest));

		ApiResponse response = service.approveLeave(1L, 100L, "Approved");

		assertEquals(403, response.getStatus());
	}

	@Test
	void rejectLeave_noComments() {

		ApiResponse response = service.rejectLeave(1L, 100L, "");

		assertEquals(400, response.getStatus());
	}

	@Test
	void rejectLeave_success() {

		when(leaveRequestRepo.findById(100L)).thenReturn(Optional.of(leaveRequest));
		when(employeeRepo.findById(1L)).thenReturn(Optional.of(manager));

		ApiResponse response = service.rejectLeave(1L, 100L, "Not valid");

		assertEquals(200, response.getStatus());

		verify(approvalRepo, times(1)).save(any(LeaveApproval.class));
		verify(notificationService, times(1)).createNotification(any());
		verify(activityLogService, times(1)).log(eq(1L), anyString());
	}

	@Test
	void getTeamLeaveBalance_success() {

		when(employeeRepo.existsById(1L)).thenReturn(true);
		when(employeeRepo.findByManager_Id(1L)).thenReturn(Collections.singletonList(employee));
		when(leaveBalanceRepo.findByEmployee_Id(2L)).thenReturn(Collections.emptyList());

		ApiResponse response = service.getTeamLeaveBalance(1L);

		assertEquals(200, response.getStatus());

		verify(activityLogService, times(1)).log(eq(1L), anyString());
	}

}