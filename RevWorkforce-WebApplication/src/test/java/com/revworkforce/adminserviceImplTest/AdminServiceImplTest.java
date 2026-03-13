package com.revworkforce.adminserviceImplTest;

import com.revworkforce.adminserviceImpl.AdminServiceImpl;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

	@InjectMocks
	private AdminServiceImpl adminService;

	@Mock
	private EmployeeRepository employeeRepository;

	@Test
	public void getEmployeeByEmail_Positive() {

		Employee employee = new Employee();
		employee.setEmail("mahitha@example.com");

		when(employeeRepository.findByEmail("mahitha@example.com")).thenReturn(Optional.of(employee));

		Optional<Employee> result = adminService.getEmployeeByEmail("mahitha@example.com");

		assertTrue(result.isPresent());
		assertEquals("mahitha@example.com", result.get().getEmail());

		verify(employeeRepository, times(1)).findByEmail("mahitha@example.com");
	}

	@Test
	public void getEmployeeByEmail_Negative() {

		when(employeeRepository.findByEmail("wrong@example.com")).thenReturn(Optional.empty());

		Optional<Employee> result = adminService.getEmployeeByEmail("wrong@example.com");

		assertFalse(result.isPresent());

		verify(employeeRepository, times(1)).findByEmail("wrong@example.com");
	}

	@Test
	public void getTotalEmployees_ShouldReturnZero() {

		long result = adminService.getTotalEmployees();

		assertEquals(0, result);
	}

	@Test
	public void getTotalManagers_ShouldReturnZero() {

		long result = adminService.getTotalManagers();

		assertEquals(0, result);
	}

	@Test
	public void getTotalRegularEmployees_ShouldReturnZero() {

		long result = adminService.getTotalRegularEmployees();

		assertEquals(0, result);
	}

	@Test
	public void getPendingLeaves_ShouldReturnZero() {

		long result = adminService.getPendingLeaves();

		assertEquals(0, result);
	}

	@Test
	public void getApprovedLeaves_ShouldReturnZero() {

		long result = adminService.getApprovedLeaves();

		assertEquals(0, result);
	}
	
}