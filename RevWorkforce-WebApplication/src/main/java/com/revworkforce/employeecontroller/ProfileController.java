package com.revworkforce.employeecontroller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;

@Controller
@RequestMapping("/employee")   // ✅ VERY IMPORTANT
public class ProfileController {

    private final EmployeeRepository employeeRepository;

    public ProfileController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/profile")    // ✅ final URL = /employee/profile
    public String employeeProfile(Model model, Principal principal) {

        Employee employee = employeeRepository
                .findByEmail(principal.getName())
                .orElseThrow();

        model.addAttribute("employee", employee);

        return "employee/profile";
    }
}