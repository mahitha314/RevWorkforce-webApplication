package com.revworkforce.managerserviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private static final Logger logger = LogManager.getLogger(TeamGoalsServiceImpl.class);

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

		logger.info("Fetching team goals for managerId {}", managerId);

		if (!employeeRepo.existsById(managerId)) {

			logger.warn("Manager not found with id {}", managerId);
			return new ApiResponse(404, "Manager not found", null);
		}

		List<Goal> goals = goalRepo.findByEmployee_Manager_Id(managerId);

		logger.debug("Total goals fetched for manager {} : {}", managerId, goals.size());

		logger.info("Manager {} viewed team goals at {}", managerId, LocalDateTime.now());

		return new ApiResponse(200, "Team goals fetched successfully", goals);
	}

	@Override
	public ApiResponse updateGoalProgress(Long managerId, GoalDTO dto) {

		logger.info("Manager {} updating goal progress for goalId {}", managerId, dto.getGoalId());

		Goal goal = goalRepo.findById(dto.getGoalId()).orElse(null);

		if (goal == null) {

			logger.error("Goal not found with id {}", dto.getGoalId());
			throw new ResourceNotFoundException("Goal not found");
		}

		if (goal.getEmployee().getManager() == null || !goal.getEmployee().getManager().getId().equals(managerId)) {

			logger.warn("Unauthorized goal update attempt by manager {}", managerId);
			return new ApiResponse(403, "Unauthorized action", null);
		}

		if (dto.getProgress() == null) {

			logger.warn("Progress value missing for goalId {}", dto.getGoalId());
			return new ApiResponse(400, "Progress is required", null);
		}

		if (dto.getProgress() < 0 || dto.getProgress() > 100) {

			logger.warn("Invalid progress value {} for goalId {}", dto.getProgress(), dto.getGoalId());
			return new ApiResponse(400, "Progress must be between 0 and 100", null);
		}

		logger.debug("Updating progress for goalId {} to {}%", dto.getGoalId(), dto.getProgress());

		goal.setProgress(dto.getProgress());

		if (dto.getProgress() == 100) {
			goal.setStatus("COMPLETED");
		} else {
			goal.setStatus("IN_PROGRESS");
		}

		goalRepo.save(goal);

		logger.info("Goal progress updated successfully for goalId {}", goal.getId());

		NotificationDTO notification = new NotificationDTO();
		notification.setEmployeeId(goal.getEmployee().getEmployeeId());
		notification.setTitle("Goal Progress Updated");
		notification.setMessage("Your goal progress has been updated to " + dto.getProgress() + "%.");
		notification.setType("PERFORMANCE");
		notification.setStatus("DELIVERED");
		notification.setIsRead(false);
		notification.setCreatedAt(LocalDateTime.now());
		notification.setReferenceId(goal.getId());

		notificationService.createNotification(notification);

		logger.info("Notification sent to employeeId {}", goal.getEmployee().getEmployeeId());

		logger.info("Manager {} updated goal {} for employee {} to {}% progress", managerId, goal.getId(),
				goal.getEmployee().getEmployeeId(), dto.getProgress());

		return new ApiResponse(200, "Goal progress updated successfully", goal);
	}

	@Override
	public ApiResponse getGoalSummary(Long managerId) {

		logger.info("Fetching goal summary for managerId {}", managerId);

		if (!employeeRepo.existsById(managerId)) {

			logger.warn("Manager not found with id {}", managerId);
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

		logger.debug("Goal summary calculated for manager {} : {}", managerId, summary);

		logger.info("Goal summary fetched successfully for managerId {}", managerId);

		return new ApiResponse(200, "Goal summary fetched successfully", summary);
	}

}