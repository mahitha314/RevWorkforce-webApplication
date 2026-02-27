
package com.revworkforce.managerservice;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.GoalDTO;

public interface TeamGoalsService {
	
	ApiResponse getTeamGoals(Long managerId);

    ApiResponse updateGoalProgress(Long managerId, GoalDTO dto);

    ApiResponse getGoalSummary(Long managerId);

}
