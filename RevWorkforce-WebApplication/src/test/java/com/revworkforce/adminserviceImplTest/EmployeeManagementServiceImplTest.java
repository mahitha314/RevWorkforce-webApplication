package com.revworkforce.adminserviceImplTest;

import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.adminserviceImpl.EmployeeManagementServiceImpl;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.EmployeeDTO;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.model.Department;
import com.revworkforce.model.Designation;
import com.revworkforce.model.Employee;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.DepartmentRepository;
import com.revworkforce.repository.DesignationRepository;
import com.revworkforce.repository.EmployeeRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeManagementServiceImplTest {

	@InjectMocks
	private EmployeeManagementServiceImpl service;

	@Mock
	private EmployeeRepository employeeRepository;
	@Mock
	private DepartmentRepository departmentRepository;
	@Mock
	private DesignationRepository designationRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private ActivityLogService activityLogService;
	@Mock
	private NotificationService notificationService;
	@Mock
	private HttpServletRequest request;

	private Employee employee;
	private EmployeeDTO dto;
	private Department department;
	private Designation designation;

	@BeforeEach
	public void setup() {

		department = new Department();
		department.setId(1L);

		designation = new Designation();
		designation.setId(1L);

		employee = new Employee();
		employee.setId(1L);
		employee.setEmployeeId("EMP001");
		employee.setFirstName("John");
		employee.setEmail("john@test.com");

		dto = new EmployeeDTO();
		dto.setEmployeeId("EMP001");
		dto.setFirstName("John");
		dto.setLastName("Doe");
		dto.setEmail("john@test.com");
		dto.setPassword("1234");
		dto.setDepartmentId(1L);
		dto.setDesignationId(1L);
		dto.setRole("EMPLOYEE");
	}

	@Test
	public void addEmployee_EmailExists() {

		when(employeeRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(employee));

		ResponseEntity<ApiResponse> response = service.addEmployee(dto);

		assertEquals(409, response.getBody().getStatus());
		verify(employeeRepository, never()).save(any());
	}

	@Test
	public void getByEmployeeId_Positive() {

		when(employeeRepository.findByEmployeeId("EMP001")).thenReturn(Optional.of(employee));

		ResponseEntity<ApiResponse> response = service.getByEmployeeId("EMP001");

		assertEquals(200, response.getBody().getStatus());
	}

	@Test
	public void deleteEmployee_Positive() {

		when(employeeRepository.findByEmployeeId("EMP001")).thenReturn(Optional.of(employee));

		ResponseEntity<ApiResponse> response = service.deleteEmployee("EMP001");

		assertEquals(200, response.getBody().getStatus());

		verify(employeeRepository).delete(employee);
		verify(activityLogService).log(anyString(), anyString(), anyString(), anyString(),
				any(HttpServletRequest.class));
	}

	@Test
	public void reactivateEmployee_Positive() {

		when(employeeRepository.findByEmployeeId("EMP001")).thenReturn(Optional.of(employee));

		ResponseEntity<ApiResponse> response = service.reactivateEmployee("EMP001", "Reason");

		assertEquals(200, response.getBody().getStatus());

		verify(employeeRepository).save(employee);
		verify(notificationService).createNotification(any(NotificationDTO.class));
	}

	@Test
	public void deactivateEmployee_Positive() {

		when(employeeRepository.findByEmployeeId("EMP001")).thenReturn(Optional.of(employee));

		ResponseEntity<ApiResponse> response = service.deactivateEmployee("EMP001", "Violation");

		assertEquals(200, response.getBody().getStatus());

		verify(employeeRepository).save(employee);
		verify(notificationService).createNotification(any(NotificationDTO.class));
	}

	@Test
	public void changeManager_Positive() {

		Employee manager = new Employee();
		manager.setId(2L);

		when(employeeRepository.findByEmployeeId("EMP001")).thenReturn(Optional.of(employee));

		when(employeeRepository.findById(2L)).thenReturn(Optional.of(manager));

		ResponseEntity<ApiResponse> response = service.changeManager("EMP001", 2L);

		assertEquals(200, response.getBody().getStatus());

		verify(employeeRepository).save(employee);
	}

	@Test
	public void searchEmployees_Positive() {

		when(employeeRepository.search("john")).thenReturn(List.of(employee));

		ResponseEntity<ApiResponse> response = service.searchEmployees("john");

		assertEquals(200, response.getBody().getStatus());
		verify(activityLogService).log(anyString(), anyString(), anyString(), anyString(),
				any(HttpServletRequest.class));
	}

	@Test
	public void countEmployees_Positive() {
		when(employeeRepository.count()).thenReturn(10L);
		assertEquals(10, service.countEmployees());
	}

	@Test
	public void countManagers_Positive() {
		when(employeeRepository.countByRole("MANAGER")).thenReturn(2L);
		assertEquals(2, service.countManagers());
	}
}