package com.revworkforce.managerserviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.GoalDTO;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.exception.ResourceNotFoundException;
import com.revworkforce.managerservice.TeamGoalsService;
import com.revworkforce.model.Goal;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.GoalRepository;

@Service
public class TeamGoalsServiceImpl implements TeamGoalsService {

	private final GoalRepository goalRepo;
	private final EmployeeRepository employeeRepo;
	private final NotificationService notificationService;

	public TeamGoalsServiceImpl(GoalRepository goalRepo, EmployeeRepository employeeRepo,
			NotificationService notificationService) {
		this.goalRepo = goalRepo;
		this.employeeRepo = employeeRepo;
		this.notificationService = notificationService;
	}

	@Override
	public ApiResponse getTeamGoals(Long managerId) {

		if (!employeeRepo.existsById(managerId)) {
			return new ApiResponse(404, "Manager not found", null);
		}

		List<Goal> goals = goalRepo.findByEmployee_Manager_Id(managerId);

		return new ApiResponse(200, "Team goals fetched successfully", goals);
	}

	@Override
	public ApiResponse updateGoalProgress(Long managerId, GoalDTO dto) {

		Goal goal = goalRepo.findById(dto.getGoalId()).orElse(null);

		if (goal == null) {
			throw new ResourceNotFoundException("Goal not found");
		}

		if (goal.getEmployee().getManager() == null || !goal.getEmployee().getManager().getId().equals(managerId)) {
			return new ApiResponse(403, "Unauthorized action", null);
		}

		if (dto.getProgress() == null) {
			return new ApiResponse(400, "Progress is required", null);
		}

		if (dto.getProgress() < 0 || dto.getProgress() > 100) {
			return new ApiResponse(400, "Progress must be between 0 and 100", null);
		}

		goal.setProgress(dto.getProgress());

		if (dto.getProgress() == 100) {
			goal.setStatus("COMPLETED");
		} else {
			goal.setStatus("IN_PROGRESS");
		}

		goalRepo.save(goal);

		NotificationDTO notification = new NotificationDTO();
		notification.setEmployeeId(goal.getEmployee().getEmployeeId());
		notification.setTitle("Goal Progress Updated");
		notification.setMessage("Your goal progress has been updated to " + dto.getProgress() + "%.");
		notification.setStatus("ACTIVE");
		notification.setType("GOAL");
		notification.setIsRead(false);
		notification.setCreatedAt(LocalDateTime.now());
		notification.setReferenceId(goal.getId());

		notificationService.createNotification(notification);

		return new ApiResponse(200, "Goal progress updated successfully", goal);
	}

	@Override
	public ApiResponse getGoalSummary(Long managerId) {

		if (!employeeRepo.existsById(managerId)) {
			return new ApiResponse(404, "Manager not found", null);
		}

		List<Goal> goals = goalRepo.findByEmployee_Manager_Id(managerId);

		long total = goals.size();
		long completed = goals.stream().filter(g -> "COMPLETED".equals(g.getStatus())).count();
		long inProgress = goals.stream().filter(g -> "IN_PROGRESS".equals(g.getStatus())).count();
		long assigned = goals.stream().filter(g -> "ASSIGNED".equals(g.getStatus())).count();

		Map<String, Object> summary = new HashMap<>();
		summary.put("totalGoals", total);
		summary.put("completedGoals", completed);
		summary.put("inProgressGoals", inProgress);
		summary.put("assignedGoals", assigned);

		return new ApiResponse(200, "Goal summary fetched successfully", summary);
	}

}