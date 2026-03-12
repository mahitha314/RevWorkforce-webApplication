package com.revworkforce.employeeservice;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.GoalDTO;

public interface GoalService {

    ApiResponse createGoal(Long employeeId, GoalDTO dto);

    ApiResponse getEmployeeGoals(Long employeeId);

    ApiResponse updateGoalProgress(Long goalId, Integer progress);

    ApiResponse deleteGoal(Long goalId);
    
}