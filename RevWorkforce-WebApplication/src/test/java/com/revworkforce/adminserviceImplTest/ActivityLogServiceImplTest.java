package com.revworkforce.adminserviceImplTest;

import com.revworkforce.adminserviceImpl.ActivityLogServiceImpl;
import com.revworkforce.model.Employee;
import com.revworkforce.model.SystemActivityLog;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.SystemActivityLogRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ActivityLogServiceImplTest {

	@InjectMocks
	private ActivityLogServiceImpl activityLogService;

	@Mock
	private SystemActivityLogRepository repository;

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private HttpServletRequest httpServletRequest;

	@BeforeEach
	public void setupSecurityContext() {
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("mahitha@example.com", null);

		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Test
	public void log_Positive() {

		// Arrange
		Employee employee = new Employee();
		employee.setId(1L);
		employee.setFirstName("Mahitha");
		employee.setRole("ADMIN");

		when(employeeRepository.findByEmail("mahitha@example.com")).thenReturn(Optional.of(employee));

		when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");

		ArgumentCaptor<SystemActivityLog> captor = ArgumentCaptor.forClass(SystemActivityLog.class);

		// Act
		activityLogService.log("CREATE", "ADMIN_MODULE", "Created employee", "SUCCESS", httpServletRequest);

		// Assert
		verify(repository, times(1)).save(captor.capture());

		SystemActivityLog savedLog = captor.getValue();

		assertEquals(1L, savedLog.getUserId());
		assertEquals("Mahitha", savedLog.getUserName());
		assertEquals("ADMIN", savedLog.getRole());
		assertEquals("CREATE", savedLog.getAction());
		assertEquals("ADMIN_MODULE", savedLog.getModule());
		assertEquals("SUCCESS", savedLog.getStatus());
		assertEquals("127.0.0.1", savedLog.getIpAddress());
		assertNotNull(savedLog.getCreatedAt());
	}

	@Test
	public void log_Negative_EmployeeNotFound() {

		when(employeeRepository.findByEmail("mahitha@example.com")).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> {
			activityLogService.log("CREATE", "ADMIN_MODULE", "Created employee", "SUCCESS", httpServletRequest);
		});

		verify(repository, never()).save(any());
	}
}