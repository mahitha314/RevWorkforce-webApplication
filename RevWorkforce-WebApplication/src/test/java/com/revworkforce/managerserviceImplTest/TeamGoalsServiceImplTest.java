package com.revworkforce.managerserviceImplTest;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.GoalDTO;
import com.revworkforce.exception.ResourceNotFoundException;
import com.revworkforce.managerserviceImpl.TeamGoalsServiceImpl;
import com.revworkforce.model.Employee;
import com.revworkforce.model.Goal;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.GoalRepository;

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
class TeamGoalsServiceImplTest {

	@Mock
	private GoalRepository goalRepo;

	@Mock
	private EmployeeRepository employeeRepo;

	@Mock
	private NotificationService notificationService;

	@InjectMocks
	private TeamGoalsServiceImpl service;

	private Employee manager;
	private Employee employee;
	private Goal goal;

	@BeforeEach
	void setup() {

		manager = new Employee();
		manager.setId(1L);

		employee = new Employee();
		employee.setId(2L);
		employee.setEmployeeId("EMP001");
		employee.setManager(manager);

		goal = new Goal();
		goal.setId(10L);
		goal.setEmployee(employee);
		goal.setStatus("ASSIGNED");
		goal.setProgress(0);
	}

	@Test
	void getTeamGoals_managerNotFound() {

		when(employeeRepo.existsById(1L)).thenReturn(false);

		ApiResponse response = service.getTeamGoals(1L);

		assertEquals(404, response.getStatus());
	}

	@Test
	void getTeamGoals_success() {

		when(employeeRepo.existsById(1L)).thenReturn(true);
		when(goalRepo.findByEmployee_Manager_Id(1L)).thenReturn(Collections.singletonList(goal));

		ApiResponse response = service.getTeamGoals(1L);

		assertEquals(200, response.getStatus());
	}

	@Test
	void updateGoalProgress_goalNotFound() {

		when(goalRepo.findById(10L)).thenReturn(Optional.empty());

		GoalDTO dto = new GoalDTO();
		dto.setGoalId(10L);

		assertThrows(ResourceNotFoundException.class, () -> service.updateGoalProgress(1L, dto));
	}

	@Test
	void updateGoalProgress_unauthorizedManager() {

		Employee otherManager = new Employee();
		otherManager.setId(99L);
		employee.setManager(otherManager);

		when(goalRepo.findById(10L)).thenReturn(Optional.of(goal));

		GoalDTO dto = new GoalDTO();
		dto.setGoalId(10L);
		dto.setProgress(50);

		ApiResponse response = service.updateGoalProgress(1L, dto);

		assertEquals(403, response.getStatus());
	}

	@Test
	void updateGoalProgress_progressNull() {

		when(goalRepo.findById(10L)).thenReturn(Optional.of(goal));

		GoalDTO dto = new GoalDTO();
		dto.setGoalId(10L);

		ApiResponse response = service.updateGoalProgress(1L, dto);

		assertEquals(400, response.getStatus());
	}

	@Test
	void updateGoalProgress_invalidProgress() {

		when(goalRepo.findById(10L)).thenReturn(Optional.of(goal));

		GoalDTO dto = new GoalDTO();
		dto.setGoalId(10L);
		dto.setProgress(150);

		ApiResponse response = service.updateGoalProgress(1L, dto);

		assertEquals(400, response.getStatus());
	}

	@Test
	void updateGoalProgress_successInProgress() {

		when(goalRepo.findById(10L)).thenReturn(Optional.of(goal));

		GoalDTO dto = new GoalDTO();
		dto.setGoalId(10L);
		dto.setProgress(60);

		ApiResponse response = service.updateGoalProgress(1L, dto);

		assertEquals(200, response.getStatus());
		assertEquals("IN_PROGRESS", goal.getStatus());

		verify(goalRepo, times(1)).save(goal);
		verify(notificationService, times(1)).createNotification(any());
	}

	@Test
	void updateGoalProgress_successCompleted() {

		when(goalRepo.findById(10L)).thenReturn(Optional.of(goal));

		GoalDTO dto = new GoalDTO();
		dto.setGoalId(10L);
		dto.setProgress(100);

		ApiResponse response = service.updateGoalProgress(1L, dto);

		assertEquals(200, response.getStatus());
		assertEquals("COMPLETED", goal.getStatus());
	}

	@Test
	void getGoalSummary_managerNotFound() {

		when(employeeRepo.existsById(1L)).thenReturn(false);

		ApiResponse response = service.getGoalSummary(1L);

		assertEquals(404, response.getStatus());
	}

	@Test
	void getGoalSummary_success() {

		when(employeeRepo.existsById(1L)).thenReturn(true);

		Goal g1 = new Goal();
		g1.setStatus("COMPLETED");

		Goal g2 = new Goal();
		g2.setStatus("IN_PROGRESS");

		Goal g3 = new Goal();
		g3.setStatus("ASSIGNED");

		when(goalRepo.findByEmployee_Manager_Id(1L)).thenReturn(Arrays.asList(g1, g2, g3));

		ApiResponse response = service.getGoalSummary(1L);

		assertEquals(200, response.getStatus());

		Map<String, Object> summary = (Map<String, Object>) response.getData();

		assertEquals(3L, summary.get("totalGoals"));
		assertEquals(1L, summary.get("completedGoals"));
		assertEquals(1L, summary.get("inProgressGoals"));
		assertEquals(1L, summary.get("assignedGoals"));
	}
}