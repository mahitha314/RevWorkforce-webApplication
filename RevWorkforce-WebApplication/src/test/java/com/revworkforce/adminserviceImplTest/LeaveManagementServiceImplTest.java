package com.revworkforce.adminserviceImplTest;

import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.adminserviceImpl.LeaveManagementServiceImpl;
import com.revworkforce.dto.LeaveBalanceDTO;
import com.revworkforce.dto.LeaveTypeDTO;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveBalance;
import com.revworkforce.model.LeaveType;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.LeaveBalanceRepository;
import com.revworkforce.repository.LeaveTypeRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeaveManagementServiceImplTest {

	@InjectMocks
	private LeaveManagementServiceImpl service;

	@Mock
	private LeaveTypeRepository leaveTypeRepository;
	@Mock
	private LeaveBalanceRepository leaveBalanceRepository;
	@Mock
	private EmployeeRepository employeeRepository;
	@Mock
	private NotificationService notificationService;
	@Mock
	private ActivityLogService activityLogService;
	@Mock
	private HttpServletRequest request;

	private Employee employee;
	private LeaveType leaveType;
	private LeaveBalance leaveBalance;

	@BeforeEach
	public void setup() {

		employee = new Employee();
		employee.setId(1L);
		employee.setEmployeeId("EMP001");
		employee.setFirstName("John");

		leaveType = new LeaveType();
		leaveType.setId(1L);
		leaveType.setTypeName("Sick Leave");
		leaveType.setTotalDays(12);

		leaveBalance = new LeaveBalance();
		leaveBalance.setId(1L);
		leaveBalance.setEmployee(employee);
		leaveBalance.setLeaveType(leaveType);
		leaveBalance.setTotalLeaves(10);
		leaveBalance.setUsedLeaves(2);
		leaveBalance.setRemainingLeaves(8);
	}

	@Test
	public void createLeaveType_Positive() {

		LeaveTypeDTO dto = new LeaveTypeDTO(null, "Sick Leave", 12);

		when(leaveTypeRepository.existsByTypeName("Sick Leave")).thenReturn(false);

		when(leaveTypeRepository.save(any(LeaveType.class))).thenReturn(leaveType);

		LeaveTypeDTO result = service.createLeaveType(dto);

		assertNotNull(result);
		assertEquals("Sick Leave", result.getTypeName());

		verify(activityLogService).log(anyString(), anyString(), anyString(), anyString(),
				any(HttpServletRequest.class));
	}

	@Test
	public void createLeaveType_Duplicate() {

		LeaveTypeDTO dto = new LeaveTypeDTO(null, "Sick Leave", 12);

		when(leaveTypeRepository.existsByTypeName("Sick Leave")).thenReturn(true);

		assertThrows(RuntimeException.class, () -> service.createLeaveType(dto));
	}

	@Test
	public void assignLeave_Positive() {

		when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

		when(leaveTypeRepository.findById(1L)).thenReturn(Optional.of(leaveType));

		when(leaveBalanceRepository.findByEmployeeAndLeaveType(employee, leaveType)).thenReturn(Optional.empty());

		when(leaveBalanceRepository.save(any(LeaveBalance.class))).thenReturn(leaveBalance);

		LeaveBalance result = service.assignLeaveToEmployee(1L, 1L, 10);

		assertNotNull(result);

		verify(notificationService).createNotification(any(NotificationDTO.class));

		verify(activityLogService).log(anyString(), anyString(), anyString(), anyString(),
				any(HttpServletRequest.class));
	}

	@Test
	public void assignLeave_EmployeeNotFound() {

		when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> service.assignLeaveToEmployee(1L, 1L, 10));
	}

	@Test
	public void adjustLeave_Positive() {

		LeaveBalanceDTO dto = new LeaveBalanceDTO();
		dto.setEmployeeId(1L);
		dto.setLeaveTypeId(1L);
		dto.setDays(5);

		when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

		when(leaveTypeRepository.findById(1L)).thenReturn(Optional.of(leaveType));

		when(leaveBalanceRepository.findByEmployeeAndLeaveType(employee, leaveType))
				.thenReturn(Optional.of(leaveBalance));

		when(leaveBalanceRepository.save(any(LeaveBalance.class))).thenReturn(leaveBalance);

		LeaveBalance result = service.adjustLeave(dto);

		assertNotNull(result);
		verify(activityLogService).log(anyString(), anyString(), anyString(), anyString(),
				any(HttpServletRequest.class));
	}

	@Test
	public void getEmployeeLeaveInfo_Positive() {

		when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

		when(leaveBalanceRepository.findByEmployee(employee)).thenReturn(List.of(leaveBalance));

		List<LeaveBalance> list = service.getEmployeeLeaveInfo(1L);

		assertEquals(1, list.size());
	}

	@Test
	public void getDepartmentLeaveReport_Positive() {

		when(leaveBalanceRepository.findByDepartmentId(1L)).thenReturn(List.of(leaveBalance));

		List<LeaveBalance> list = service.getDepartmentLeaveReport(1L);

		assertEquals(1, list.size());
	}

	@Test
	public void countLeaves_Positive() {

		when(leaveTypeRepository.count()).thenReturn(5L);

		assertEquals(5, service.countLeaves());
	}
}