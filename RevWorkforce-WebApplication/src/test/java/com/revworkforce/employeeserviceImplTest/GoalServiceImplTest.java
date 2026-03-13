package com.revworkforce.employeeserviceImplTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.GoalDTO;
import com.revworkforce.exception.EmployeeNotFoundException;
import com.revworkforce.exception.GoalUpdateException;
import com.revworkforce.model.Employee;
import com.revworkforce.model.Goal;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.GoalRepository;
import com.revworkforce.employeeserviceImpl.GoalServiceImpl;

@ExtendWith(MockitoExtension.class)
public class GoalServiceImplTest {

	@InjectMocks
	private GoalServiceImpl service;

	@Mock
	private GoalRepository goalRepo;

	@Mock
	private EmployeeRepository employeeRepo;

	@Mock
	private NotificationService notificationService;

	@Mock
	private ActivityLogService activityLogService;

	private Employee employee;
	private Goal goal;
	private GoalDTO dto;

	@BeforeEach
	void setUp() {

		employee = new Employee();
		employee.setId(1L);
		employee.setEmployeeId("EMP001");
		employee.setFirstName("John");
		employee.setLastName("Doe");

		goal = new Goal();
		goal.setId(1L);
		goal.setEmployee(employee);
		goal.setGoalDescription("Finish project");
		goal.setPriority("HIGH");
		goal.setStatus("NOT_STARTED");
		goal.setProgress(0);

		dto = new GoalDTO();
		dto.setGoalDescription("Finish project");
		dto.setPriority("HIGH");
		dto.setDeadline(LocalDate.now().plusDays(10));
	}

	@Test
	public void createGoal_Positive() {

		when(employeeRepo.findById(1L)).thenReturn(Optional.of(employee));

		when(goalRepo.save(any(Goal.class))).thenReturn(goal);

		ApiResponse response = service.createGoal(1L, dto);

		assertEquals(201, response.getStatus());
		verify(notificationService).createNotification(any());
		verify(activityLogService).log(anyString(), anyString());
	}

	@Test
	public void createGoal_EmployeeNotFound() {

		when(employeeRepo.findById(1L)).thenReturn(Optional.empty());

		assertThrows(EmployeeNotFoundException.class, () -> service.createGoal(1L, dto));
	}

	@Test
	public void getEmployeeGoals_Positive() {

		when(employeeRepo.findById(1L)).thenReturn(Optional.of(employee));

		when(goalRepo.findByEmployee_Id(1L)).thenReturn(Arrays.asList(goal));

		ApiResponse response = service.getEmployeeGoals(1L);

		assertEquals(200, response.getStatus());
		assertNotNull(response.getData());
	}

	@Test
	public void getEmployeeGoals_EmployeeNotFound() {

		when(employeeRepo.findById(1L)).thenReturn(Optional.empty());

		assertThrows(EmployeeNotFoundException.class, () -> service.getEmployeeGoals(1L));
	}

	@Test
	public void updateGoalProgress_ToInProgress() {

		when(goalRepo.findById(1L)).thenReturn(Optional.of(goal));

		ApiResponse response = service.updateGoalProgress(1L, 50);

		assertEquals(200, response.getStatus());
		assertEquals("IN_PROGRESS", goal.getStatus());

		verify(goalRepo).save(goal);
		verify(activityLogService).log(anyString(), anyString());
	}

	@Test
	public void updateGoalProgress_ToCompleted() {

		when(goalRepo.findById(1L)).thenReturn(Optional.of(goal));

		ApiResponse response = service.updateGoalProgress(1L, 100);

		assertEquals("COMPLETED", goal.getStatus());
		verify(notificationService).createNotification(any());
	}

	@Test
	public void updateGoalProgress_InvalidProgress() {

		when(goalRepo.findById(1L)).thenReturn(Optional.of(goal));

		assertThrows(GoalUpdateException.class, () -> service.updateGoalProgress(1L, 120));
	}

	@Test
	public void updateGoalProgress_GoalNotFound() {

		when(goalRepo.findById(1L)).thenReturn(Optional.empty());

		assertThrows(GoalUpdateException.class, () -> service.updateGoalProgress(1L, 50));
	}

	@Test
	public void deleteGoal_Positive() {

		goal.setStatus("IN_PROGRESS");

		when(goalRepo.findById(1L)).thenReturn(Optional.of(goal));

		ApiResponse response = service.deleteGoal(1L);

		assertEquals(200, response.getStatus());

		verify(goalRepo).delete(goal);
		verify(activityLogService).log(anyString(), anyString());
	}

	@Test
	public void deleteGoal_CompletedGoal() {

		goal.setStatus("COMPLETED");

		when(goalRepo.findById(1L)).thenReturn(Optional.of(goal));

		assertThrows(GoalUpdateException.class, () -> service.deleteGoal(1L));
	}

	@Test
	public void deleteGoal_NotFound() {

		when(goalRepo.findById(1L)).thenReturn(Optional.empty());

		assertThrows(GoalUpdateException.class, () -> service.deleteGoal(1L));
	}

}