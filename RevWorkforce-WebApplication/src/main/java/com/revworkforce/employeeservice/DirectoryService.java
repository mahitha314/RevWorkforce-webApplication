package com.revworkforce.employeeservice;

import com.revworkforce.dto.ApiResponse;

public interface DirectoryService {

    ApiResponse getAllEmployees();

    ApiResponse searchEmployees(String keyword);

}