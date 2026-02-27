
package com.revworkforce.managercontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.GoalDTO;
import com.revworkforce.managerservice.TeamGoalsService;

@RestController
@RequestMapping("/manager/goals")
public class TeamGoalsController {

    private final TeamGoalsService service;

    public TeamGoalsController(TeamGoalsService service) {
        this.service = service;
    }

    @GetMapping("/{managerId}")
    public ResponseEntity<ApiResponse> getGoals(@PathVariable Long managerId) {

        ApiResponse response = service.getTeamGoals(managerId);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @PutMapping("/update/{managerId}")
    public ResponseEntity<ApiResponse> updateGoal(
            @PathVariable Long managerId,
            @RequestBody GoalDTO dto) {

        ApiResponse response =
                service.updateGoalProgress(managerId, dto);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping("/summary/{managerId}")
    public ResponseEntity<ApiResponse> goalSummary(
            @PathVariable Long managerId) {

        ApiResponse response =
                service.getGoalSummary(managerId);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
    
}
