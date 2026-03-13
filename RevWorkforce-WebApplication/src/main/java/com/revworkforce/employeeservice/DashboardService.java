package com.revworkforce.employeeservice;

import com.revworkforce.dto.ApiResponse;

public interface DashboardService {

    ApiResponse getEmployeeDashboard(Long employeeId);

}