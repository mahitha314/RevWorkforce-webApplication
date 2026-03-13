package com.revworkforce.employeeserviceImpl;

import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.GoalDTO;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.employeeservice.GoalService;
import com.revworkforce.exception.EmployeeNotFoundException;
import com.revworkforce.exception.GoalUpdateException;
import com.revworkforce.model.Employee;
import com.revworkforce.model.Goal;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.GoalRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalServiceImpl implements GoalService {

	private static final Logger logger = LogManager.getLogger(GoalServiceImpl.class);

	private final GoalRepository goalRepo;
	private final EmployeeRepository employeeRepo;
	private final NotificationService notificationService;
	private final ActivityLogService activityLogService;

	public GoalServiceImpl(GoalRepository goalRepo, EmployeeRepository employeeRepo,
			NotificationService notificationService, ActivityLogService activityLogService) {

		this.goalRepo = goalRepo;
		this.employeeRepo = employeeRepo;
		this.notificationService = notificationService;
		this.activityLogService = activityLogService;
	}

	@Override
	public ApiResponse createGoal(Long employeeId, GoalDTO dto) {

		logger.info("Creating goal for employee {}", employeeId);

		Employee employee = employeeRepo.findById(employeeId)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

		Goal goal = new Goal();
		goal.setEmployee(employee);
		goal.setGoalDescription(dto.getGoalDescription());
		goal.setPriority(dto.getPriority());
		goal.setStatus("NOT_STARTED");
		goal.setDeadline(dto.getDeadline());
		goal.setProgress(0);

		Goal savedGoal = goalRepo.save(goal);

		logger.info("Goal created successfully with id {}", savedGoal.getId());

		NotificationDTO notificationDTO = new NotificationDTO();
		notificationDTO.setEmployeeId(goal.getEmployee().getEmployeeId());
		notificationDTO.setTitle("New Goal Assigned");
		notificationDTO.setMessage("A new goal has been assigned to you.");
		notificationDTO.setType("GOAL");

		notificationService.createNotification(notificationDTO);

		activityLogService.log("GOAL_CREATED",
				"Goal created for employee: " + employee.getFirstName() + " " + employee.getLastName());

		return new ApiResponse(201, "Goal created successfully", mapToDTO(savedGoal));
	}

	@Override
	public ApiResponse getEmployeeGoals(Long employeeId) {

		logger.info("Fetching goals for employee {}", employeeId);

		employeeRepo.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

		List<GoalDTO> goals = goalRepo.findByEmployee_Id(employeeId).stream().map(this::mapToDTO)
				.collect(Collectors.toList());

		logger.debug("Total goals fetched {}", goals.size());

		return new ApiResponse(200, "Goals fetched successfully", goals);
	}

	@Override
	public ApiResponse updateGoalProgress(Long goalId, Integer progress) {

		logger.info("Updating goal progress for goal {}", goalId);

		Goal goal = goalRepo.findById(goalId).orElseThrow(() -> new GoalUpdateException("Goal not found"));

		if (progress == null || progress < 0 || progress > 100) {

			logger.warn("Invalid progress value {} for goal {}", progress, goalId);

			throw new GoalUpdateException("Progress must be between 0 and 100");
		}

		goal.setProgress(progress);

		if (progress == 0)
			goal.setStatus("NOT_STARTED");
		else if (progress == 100)
			goal.setStatus("COMPLETED");
		else
			goal.setStatus("IN_PROGRESS");

		goalRepo.save(goal);

		logger.info("Goal progress updated successfully to {}%", progress);

		activityLogService.log("GOAL_UPDATED", "Goal progress updated to " + progress + "%");

		if (progress == 100) {

			logger.info("Goal completed for employee {}", goal.getEmployee().getEmployeeId());

			NotificationDTO notificationDTO = new NotificationDTO();
			notificationDTO.setEmployeeId(goal.getEmployee().getEmployeeId());
			notificationDTO.setTitle("Goal Completed");
			notificationDTO.setMessage("Congratulations! You have completed your goal.");
			notificationDTO.setType("GOAL");

			notificationService.createNotification(notificationDTO);
		}

		return new ApiResponse(200, "Goal progress updated successfully", null);
	}

	@Override
	public ApiResponse deleteGoal(Long goalId) {

		logger.info("Deleting goal {}", goalId);

		Goal goal = goalRepo.findById(goalId).orElseThrow(() -> new GoalUpdateException("Goal not found"));

		if ("COMPLETED".equalsIgnoreCase(goal.getStatus())) {

			logger.warn("Attempt to delete completed goal {}", goalId);

			throw new GoalUpdateException("Completed goal cannot be deleted");
		}

		goalRepo.delete(goal);

		logger.info("Goal deleted successfully {}", goalId);

		activityLogService.log("GOAL_DELETED", "Goal deleted for employee: " + goal.getEmployee().getFirstName());

		return new ApiResponse(200, "Goal deleted successfully", null);
	}

	private GoalDTO mapToDTO(Goal goal) {
		return new GoalDTO(goal.getId(), goal.getEmployee().getId(),
				goal.getEmployee().getFirstName() + " " + goal.getEmployee().getLastName(), goal.getGoalDescription(),
				goal.getPriority(), goal.getStatus(), goal.getDeadline(), goal.getProgress());
	}

}