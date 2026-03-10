package com.revworkforce.employeeserviceImplTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.exception.EmployeeNotFoundException;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveBalance;
import com.revworkforce.model.LeaveType;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.LeaveBalanceRepository;
import com.revworkforce.employeeserviceImpl.LeaveBalanceServiceImpl;

@ExtendWith(MockitoExtension.class)
public class LeaveBalanceServiceImplTest {

	@InjectMocks
	private LeaveBalanceServiceImpl service;

	@Mock
	private LeaveBalanceRepository leaveBalanceRepo;

	@Mock
	private EmployeeRepository employeeRepo;

	private Employee employee;
	private LeaveBalance leaveBalance;
	private LeaveType leaveType;

	@BeforeEach
	void setUp() {

		employee = new Employee();
		employee.setId(1L);
		employee.setFirstName("John");

		leaveType = new LeaveType();
		leaveType.setId(10L);
		leaveType.setTypeName("Sick Leave");

		leaveBalance = new LeaveBalance();
		leaveBalance.setId(100L);
		leaveBalance.setEmployee(employee);
		leaveBalance.setLeaveType(leaveType);
		leaveBalance.setTotalLeaves(20);
		leaveBalance.setUsedLeaves(5);
		leaveBalance.setRemainingLeaves(15);
	}

	@Test
	public void getEmployeeBalances_Positive() {

		when(employeeRepo.findById(1L)).thenReturn(Optional.of(employee));

		when(leaveBalanceRepo.findByEmployee_Id(1L)).thenReturn(Arrays.asList(leaveBalance));

		ApiResponse response = service.getEmployeeBalances(1L);

		assertEquals(200, response.getStatus());
		assertEquals("Leave balances fetched successfully", response.getMessage());

		assertNotNull(response.getData());

		verify(employeeRepo).findById(1L);
		verify(leaveBalanceRepo).findByEmployee_Id(1L);
	}

	@Test
	public void getEmployeeBalances_EmployeeNotFound() {

		when(employeeRepo.findById(1L)).thenReturn(Optional.empty());

		assertThrows(EmployeeNotFoundException.class, () -> service.getEmployeeBalances(1L));

		verify(employeeRepo).findById(1L);
		verifyNoInteractions(leaveBalanceRepo);
	}

	@Test
	public void getEmployeeBalances_EmptyList() {

		when(employeeRepo.findById(1L)).thenReturn(Optional.of(employee));

		when(leaveBalanceRepo.findByEmployee_Id(1L)).thenReturn(Collections.emptyList());

		ApiResponse response = service.getEmployeeBalances(1L);

		assertEquals(200, response.getStatus());
		assertTrue(((java.util.List<?>) response.getData()).isEmpty());
	}
}