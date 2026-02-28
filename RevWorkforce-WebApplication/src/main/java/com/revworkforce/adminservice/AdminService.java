package com.revworkforce.adminservice;


import com.revworkforce.model.Employee;

import java.util.Optional;

public interface AdminService{
    Optional<Employee> getEmployeeByEmail(String email);
    long getTotalEmployees();

    long getTotalManagers();

    long getTotalRegularEmployees();

    long getPendingLeaves();

    long getApprovedLeaves();
}
