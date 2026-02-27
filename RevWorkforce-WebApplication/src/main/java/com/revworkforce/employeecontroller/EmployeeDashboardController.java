package com.revworkforce.employeecontroller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;

@Controller
public class EmployeeDashboardController {

    private final EmployeeRepository employeeRepository;

    public EmployeeDashboardController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/employee/dashboard")
    public String employeeDashboard(Model model, Principal principal) {

        Employee employee = employeeRepository
                .findByEmail(principal.getName())
                .orElseThrow();

        model.addAttribute("employeeId", employee.getId());

        return "employee/dashboard";
    }
}