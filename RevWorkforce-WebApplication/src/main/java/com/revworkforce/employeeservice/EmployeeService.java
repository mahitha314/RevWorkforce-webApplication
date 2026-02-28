package com.revworkforce.employeeservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revworkforce.model.Employee;

@Service
public interface EmployeeService {


    Employee createEmployee(Employee employee);
}