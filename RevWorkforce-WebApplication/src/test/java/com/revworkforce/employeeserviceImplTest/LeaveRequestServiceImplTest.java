package com.revworkforce.employeeserviceImplTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.LeaveRequestDTO;
import com.revworkforce.model.*;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.*;
import com.revworkforce.employeeserviceImpl.LeaveRequestServiceImpl;

@ExtendWith(MockitoExtension.class)
public class LeaveRequestServiceImplTest {

	@InjectMocks
	private LeaveRequestServiceImpl service;

	@Mock
	private LeaveRequestRepository leaveRequestRepo;
	@Mock
	private EmployeeRepository employeeRepo;
	@Mock
	private LeaveTypeRepository leaveTypeRepo;
	@Mock
	private NotificationService notificationService;
	@Mock
	private ActivityLogService activityLogService;
	@Mock
	private HttpServletRequest httpRequest;

	private Employee employee;
	private LeaveType leaveType;
	private LeaveRequest leaveRequest;
	private LeaveApproval approval;
	private LeaveRequestDTO dto;

	@BeforeEach
	void setUp() {

		employee = new Employee();
		employee.setId(1L);
		employee.setFirstName("John");
		employee.setLastName("Doe");

		leaveType = new LeaveType();
		leaveType.setId(10L);
		leaveType.setTypeName("Sick Leave");

		approval = new LeaveApproval();
		approval.setStatus("PENDING");

		leaveRequest = new LeaveRequest();
		leaveRequest.setId(100L);
		leaveRequest.setEmployee(employee);
		leaveRequest.setLeaveType(leaveType);
		leaveRequest.setStartDate(LocalDate.now());
		leaveRequest.setEndDate(LocalDate.now().plusDays(2));
		leaveRequest.setReason("Medical");
		leaveRequest.setLeaveApproval(approval);

		dto = new LeaveRequestDTO();
		dto.setEmployeeId(1L);
		dto.setLeaveTypeId(10L);
		dto.setStartDate(LocalDate.now());
		dto.setEndDate(LocalDate.now().plusDays(2));
		dto.setReason("Medical");
	}

	@Test
	public void applyLeave_EmployeeNotFound() {

		when(employeeRepo.findById(1L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> service.applyLeave(dto));
	}

	@Test
	public void cancelLeave_NotPending() {

		approval.setStatus("APPROVED");

		when(leaveRequestRepo.findById(100L)).thenReturn(Optional.of(leaveRequest));

		ApiResponse response = service.cancelLeave(100L);

		assertEquals(400, response.getStatus());
	}

	@Test
	public void cancelLeave_NotFound() {

		when(leaveRequestRepo.findById(100L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> service.cancelLeave(100L));
	}

	@Test
	public void getLeaveHistory_Positive() {

		when(leaveRequestRepo.findByEmployee_IdOrderByStartDateDesc(1L)).thenReturn(Arrays.asList(leaveRequest));

		ApiResponse response = service.getLeaveHistory(1L);

		assertEquals(200, response.getStatus());
		assertNotNull(response.getData());
	}

	@Test
	public void getPendingLeaves_Positive() {

		when(leaveRequestRepo.findByEmployee_Id(1L)).thenReturn(Arrays.asList(leaveRequest));

		ApiResponse response = service.getPendingLeaves(1L);

		assertEquals(200, response.getStatus());
	}

	@Test
	public void getPendingLeaves_Empty() {

		when(leaveRequestRepo.findByEmployee_Id(1L)).thenReturn(Collections.emptyList());

		ApiResponse response = service.getPendingLeaves(1L);

		assertTrue(((java.util.List<?>) response.getData()).isEmpty());
	}

	@Test
	public void getLeaveById_Positive() {

		when(leaveRequestRepo.findById(100L)).thenReturn(Optional.of(leaveRequest));

		ApiResponse response = service.getLeaveById(100L);

		assertEquals(200, response.getStatus());
		assertNotNull(response.getData());
	}

	@Test
	public void getLeaveById_NotFound() {

		when(leaveRequestRepo.findById(100L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> service.getLeaveById(100L));
	}

}