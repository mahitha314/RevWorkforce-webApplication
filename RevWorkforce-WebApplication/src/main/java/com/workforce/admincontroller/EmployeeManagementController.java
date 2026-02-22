package com.workforce.admincontroller;

import com.workforce.adminservice.EmployeeManagementService;
import com.workforce.model.Employee;
import com.workforce.repository.EmployeeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class EmployeeManagementController {

    private final EmployeeManagementService employeeService;
    private final EmployeeRepository employeeRepository;

    public EmployeeManagementController(EmployeeManagementService employeeService,
                                        EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {

        String email = authentication.getName();
        Employee admin = employeeRepository.findByEmail(email).orElse(null);

        model.addAttribute("adminName", admin.getFirstName());
        model.addAttribute("today", LocalDate.now());

        model.addAttribute("totalEmployees", employeeService.getEmployeeCount());
        model.addAttribute("activeEmployees", employeeService.getActiveEmployeeCount());
        model.addAttribute("inactiveEmployees", employeeService.getInactiveEmployeeCount());

        model.addAttribute("page", "dashboard");

        return "admin/employee_managementt";
    }

    // ================= EMPLOYEE LIST =================
    @GetMapping("/employees")
    public String employeeList(Model model) {

        model.addAttribute("page", "employeeList");
        model.addAttribute("employees", employeeService.getAllEmployees());

        return "admin/employee_managementt";
    }

    // ================= DELETE =================
    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/admin/employees";
    }

    // ================= ACTIVATE =================
    @GetMapping("/employees/activate/{id}")
    public String activateEmployee(@PathVariable Long id) {
        employeeService.activateEmployee(id);
        return "redirect:/admin/employees";
    }

    // ================= DEACTIVATE =================
    @GetMapping("/employees/deactivate/{id}")
    public String deactivateEmployee(@PathVariable Long id) {
        employeeService.deactivateEmployee(id);
        return "redirect:/admin/employees";
    }
}