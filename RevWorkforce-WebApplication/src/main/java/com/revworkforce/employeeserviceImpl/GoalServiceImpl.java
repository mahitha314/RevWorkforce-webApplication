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
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalServiceImpl implements GoalService {

	private final GoalRepository goalRepo;
	private final EmployeeRepository employeeRepo;
	private final NotificationService notificationService;
	private final ActivityLogService activityLogService;

	public GoalServiceImpl(GoalRepository goalRepo, EmployeeRepository employeeRepo,
			NotificationService notificationService, ActivityLogService activityLogService) {
		super();
		this.goalRepo = goalRepo;
		this.employeeRepo = employeeRepo;
		this.notificationService = notificationService;
		this.activityLogService = activityLogService;
	}

	@Override
	public ApiResponse createGoal(Long employeeId, GoalDTO dto) {

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

		employeeRepo.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

		List<GoalDTO> goals = goalRepo.findByEmployee_Id(employeeId).stream().map(this::mapToDTO)
				.collect(Collectors.toList());

		return new ApiResponse(200, "Goals fetched successfully", goals);
	}

	@Override
	public ApiResponse updateGoalProgress(Long goalId, Integer progress) {

		Goal goal = goalRepo.findById(goalId).orElseThrow(() -> new GoalUpdateException("Goal not found"));

		if (progress == null || progress < 0 || progress > 100) {
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

		activityLogService.log("GOAL_UPDATED", "Goal progress updated to " + progress + "%");

		if (progress == 100) {
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

		Goal goal = goalRepo.findById(goalId).orElseThrow(() -> new GoalUpdateException("Goal not found"));

		if ("COMPLETED".equalsIgnoreCase(goal.getStatus())) {
			throw new GoalUpdateException("Completed goal cannot be deleted");
		}

		goalRepo.delete(goal);

		activityLogService.log("GOAL_DELETED", "Goal deleted for employee: " + goal.getEmployee().getFirstName());

		return new ApiResponse(200, "Goal deleted successfully", null);
	}

	private GoalDTO mapToDTO(Goal goal) {
		return new GoalDTO(goal.getId(), goal.getEmployee().getId(),
				goal.getEmployee().getFirstName() + " " + goal.getEmployee().getLastName(), goal.getGoalDescription(),
				goal.getPriority(), goal.getStatus(), goal.getDeadline(), goal.getProgress());
	}
	
}