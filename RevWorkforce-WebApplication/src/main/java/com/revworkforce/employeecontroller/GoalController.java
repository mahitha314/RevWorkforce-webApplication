package com.revworkforce.employeecontroller;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.GoalDTO;
import com.revworkforce.employeeservice.GoalService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goals")
public class GoalController {

	private final GoalService goalService;

	public GoalController(GoalService goalService) {
		this.goalService = goalService;
	}

	@PostMapping("/{employeeId}")
	public ApiResponse createGoal(@PathVariable Long employeeId, @RequestBody GoalDTO dto) {
		return goalService.createGoal(employeeId, dto);
	}

	@GetMapping("/{employeeId}")
	public ApiResponse getGoals(@PathVariable Long employeeId) {
		return goalService.getEmployeeGoals(employeeId);
	}

	@PutMapping("/progress/{goalId}")
	public ApiResponse updateProgress(@PathVariable Long goalId, @RequestParam Integer progress) {
		return goalService.updateGoalProgress(goalId, progress);
	}

	@DeleteMapping("/{goalId}")
	public ApiResponse deleteGoal(@PathVariable Long goalId) {
		return goalService.deleteGoal(goalId);
	}

}