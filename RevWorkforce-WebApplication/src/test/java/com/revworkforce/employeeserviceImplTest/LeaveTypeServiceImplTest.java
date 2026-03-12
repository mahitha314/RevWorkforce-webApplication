package com.revworkforce.employeeserviceImplTest;

import com.revworkforce.model.LeaveType;
import com.revworkforce.repository.LeaveTypeRepository;
import com.revworkforce.employeeserviceImpl.LeaveTypeServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeaveTypeServiceImplTest {

	@Mock
	private LeaveTypeRepository leaveTypeRepository;

	@InjectMocks
	private LeaveTypeServiceImpl leaveTypeService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetAllLeaveTypes() {
		// Arrange
		LeaveType leave1 = new LeaveType();
		leave1.setId(1L);
		leave1.setTypeName("Sick Leave");

		LeaveType leave2 = new LeaveType();
		leave2.setId(2L);
		leave2.setTypeName("Casual Leave");

		List<LeaveType> mockList = Arrays.asList(leave1, leave2);

		when(leaveTypeRepository.findAll()).thenReturn(mockList);

		// Act
		List<LeaveType> result = leaveTypeService.getAllLeaveTypes();

		// Assert
		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("Sick Leave", result.get(0).getTypeName());

		verify(leaveTypeRepository, times(1)).findAll();
	}
}