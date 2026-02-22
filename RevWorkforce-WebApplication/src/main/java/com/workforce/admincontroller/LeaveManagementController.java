package com.workforce.admincontroller;

import com.workforce.adminservice.LeaveManagementService;
import com.workforce.model.Employee;
import com.workforce.model.Holiday;
import com.workforce.repository.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/leaves")
public class LeaveManagementController {

    private final LeaveManagementService leaveService;
    private final EmployeeRepository employeeRepository;

    public LeaveManagementController(LeaveManagementService leaveService,
                                     EmployeeRepository employeeRepository) {
        this.leaveService = leaveService;
        this.employeeRepository = employeeRepository;
    }

    // ================= MAIN PAGE =================
    @GetMapping
    public String leaveDashboard(Model model) {

        model.addAttribute("page", "leaveManagement");
        model.addAttribute("leaveRequests", leaveService.getAllLeaveRequests());
        model.addAttribute("pendingCount", leaveService.getPendingCount());
        model.addAttribute("approvedCount", leaveService.getApprovedCount());
        model.addAttribute("rejectedCount", leaveService.getRejectedCount());
        model.addAttribute("holiday", new Holiday());
        model.addAttribute("holidays", leaveService.getAllHolidays());

        return "admin/employee_managementt";
    }

    // ================= APPROVE =================
    @GetMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {
        leaveService.approveLeave(id);
        return "redirect:/admin/leaves";
    }

    // ================= REJECT =================
    @GetMapping("/reject/{id}")
    public String reject(@PathVariable Long id) {
        leaveService.rejectLeave(id);
        return "redirect:/admin/leaves";
    }

    // ================= ADD HOLIDAY =================
    @PostMapping("/holiday/add")
    public String addHoliday(@ModelAttribute Holiday holiday) {
        leaveService.addHoliday(holiday);
        return "redirect:/admin/leaves";
    }

    // ================= DELETE HOLIDAY =================
    @GetMapping("/holiday/delete/{id}")
    public String deleteHoliday(@PathVariable Long id) {
        leaveService.deleteHoliday(id);
        return "redirect:/admin/leaves";
    }

    // ================= LEAVE BALANCE =================
    @GetMapping("/balance/{empId}")
    public String leaveBalance(@PathVariable Long empId, Model model) {

        Employee employee = employeeRepository.findById(empId).orElseThrow();

        model.addAttribute("page", "leaveBalance");
        model.addAttribute("balances",
                leaveService.getEmployeeLeaveBalance(employee));
        model.addAttribute("employee", employee);

        return "admin/employee_managementt";
    }

    // ================= LEAVE HISTORY =================
    @GetMapping("/history/{empId}")
    public String leaveHistory(@PathVariable Long empId, Model model) {

        Employee employee = employeeRepository.findById(empId).orElseThrow();

        model.addAttribute("page", "leaveHistory");
        model.addAttribute("history",
                leaveService.getLeaveHistory(employee));

        return "admin/employee_managementt";
    }

    // ================= HOLIDAY CALENDAR =================
    @GetMapping("/calendar")
    public String holidayCalendar(Model model) {

        model.addAttribute("page", "holidayCalendar");
        model.addAttribute("holidays", leaveService.getAllHolidays());

        return "admin/employee_managementt";
    }
}