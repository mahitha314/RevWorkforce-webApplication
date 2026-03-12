package com.revworkforce.employeeservice;

import com.revworkforce.dto.ApiResponse;

public interface LeaveBalanceService {

    ApiResponse getEmployeeBalances(Long employeeId);
    
}