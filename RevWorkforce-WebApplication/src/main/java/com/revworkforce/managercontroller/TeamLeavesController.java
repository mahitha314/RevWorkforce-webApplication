package com.revworkforce.managercontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.managerservice.TeamLeavesService;

@RestController
@RequestMapping("/manager/leaves")
public class TeamLeavesController {

    private final TeamLeavesService service;

    public TeamLeavesController(TeamLeavesService service) {
        this.service = service;
    }

    @GetMapping("/team/{managerId}")
    public ResponseEntity<ApiResponse> getTeam(@PathVariable Long managerId) {

        ApiResponse response = service.getDirectReportees(managerId);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping("/requests/{managerId}")
    public ResponseEntity<ApiResponse> getRequests(@PathVariable Long managerId) {

        ApiResponse response = service.getTeamLeaveRequests(managerId);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @PostMapping("/approve/{managerId}/{leaveId}")
    public ResponseEntity<ApiResponse> approve(
            @PathVariable Long managerId,
            @PathVariable Long leaveId,
            @RequestParam(required = false) String comments) {

        ApiResponse response =
                service.approveLeave(managerId, leaveId, comments);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @PostMapping("/reject/{managerId}/{leaveId}")
    public ResponseEntity<ApiResponse> reject(
            @PathVariable Long managerId,
            @PathVariable Long leaveId,
            @RequestParam String comments) {

        ApiResponse response =
                service.rejectLeave(managerId, leaveId, comments);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping("/calendar/{managerId}")
    public ResponseEntity<ApiResponse> calendar(@PathVariable Long managerId) {

        ApiResponse response =
                service.getTeamLeaveCalendar(managerId);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping("/balance/{managerId}")
    public ResponseEntity<ApiResponse> balance(@PathVariable Long managerId) {

        ApiResponse response =
                service.getTeamLeaveBalance(managerId);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
    
}