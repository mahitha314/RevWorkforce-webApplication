package com.revworkforce.employeecontroller;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.employeeservice.DashboardService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@RestController
@RequestMapping("/api/employee")
public class EmployeeDashboardController {

    private final DashboardService dashboardService;

    public EmployeeDashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard/{employeeId}")
    public ResponseEntity<ApiResponse> getDashboard(
            @PathVariable Long employeeId) {

        ApiResponse response =
                dashboardService.getEmployeeDashboard(employeeId);

        return ResponseEntity.ok(response);
    }
}