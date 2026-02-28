package com.revworkforce.managercontroller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final EmployeeRepository employeeRepository;

    public ManagerController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/dashboard")
    public String managerDashboard(Authentication authentication, Model model) {

        String email = authentication.getName();

        Employee manager = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("manager", manager);
        model.addAttribute("employeeName", manager.getFirstName());
        model.addAttribute("managerId", manager.getId()); 
//        model.addAttribute("employeeName", employee.getFirstName());
//        model.addAttribute("manager", employee);
        return "manager/manager-dashboard";
    }
}