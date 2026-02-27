package com.revworkforce.employeecontroller;

import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveBalance;
import com.revworkforce.model.LeaveRequest;
import com.revworkforce.model.LeaveType;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.LeaveBalanceRepository;
import com.revworkforce.repository.LeaveRequestRepository;
import com.revworkforce.repository.LeaveTypeRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employee")
public class EmployeePageController {

    private final EmployeeRepository employeeRepo;
    private final LeaveBalanceRepository leaveBalanceRepo;
    private final LeaveTypeRepository leaveTypeRepo;
    private final LeaveRequestRepository leaveRequestRepo;

    public EmployeePageController(EmployeeRepository employeeRepo,
                                  LeaveBalanceRepository leaveBalanceRepo,
                                  LeaveTypeRepository leaveTypeRepo,
                                  LeaveRequestRepository leaveRequestRepo) {
        this.employeeRepo = employeeRepo;
        this.leaveBalanceRepo = leaveBalanceRepo;
        this.leaveTypeRepo = leaveTypeRepo;
        this.leaveRequestRepo = leaveRequestRepo;
    }

    // ================= GET LOGGED EMPLOYEE =================
    private Employee getEmployee() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        return employeeRepo.findByEmail(username)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));
    }

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        Employee employee = getEmployee();

        model.addAttribute("employee", employee);
        model.addAttribute("view", "dashboard");

        return "employee/dashboard";
    }

    // ================= PROFILE =================
    @GetMapping("/profile")
    public String profile(Model model) {

        Employee employee = getEmployee();

        model.addAttribute("employee", employee);
        model.addAttribute("view", "profile");

        return "employee/profile";
    }

    // ================= APPLY LEAVE PAGE =================
    @GetMapping("/apply-leave")
    public String applyLeavePage(Model model) {

        Employee employee = getEmployee();

        model.addAttribute("employee", employee);
        model.addAttribute("leaveTypes", leaveTypeRepo.findAll());
        model.addAttribute("view", "apply-leave");

        return "employee/apply_leave";
    }

    // ================= APPLY LEAVE SUBMIT =================
    @PostMapping("/apply-leave")
    public String applyLeave(@RequestParam Long leaveTypeId,
                             @RequestParam String startDate,
                             @RequestParam String endDate,
                             @RequestParam String reason) {

        Employee employee = getEmployee();

        LeaveType leaveType =
                leaveTypeRepo.findById(leaveTypeId)
                        .orElseThrow(() -> new RuntimeException("Leave type not found"));

        LeaveRequest request = new LeaveRequest();
        request.setEmployee(employee);
        request.setLeaveType(leaveType);
        request.setStartDate(LocalDate.parse(startDate));
        request.setEndDate(LocalDate.parse(endDate));
        request.setReason(reason);
        request.setStatus("PENDING");

        leaveRequestRepo.save(request);

        return "redirect:/employee/leave-status";
    }

    // ================= LEAVE STATUS (ALL LEAVES) =================
    @GetMapping("/leave-status")
    public String leaveStatus(Model model) {

        Employee employee = getEmployee();

        List<LeaveRequest> leaves =
                employee.getLeaveRequests();   // no repository error

        model.addAttribute("employee", employee);
        model.addAttribute("leaveRequests", leaves);
        model.addAttribute("view", "leave-status");

        return "employee/leave_status";
    }

    // ================= CANCEL LEAVE =================
    @PostMapping("/cancel-leave/{id}")
    public String cancelLeave(@PathVariable Long id) {

        Employee employee = getEmployee();

        LeaveRequest leave =
                leaveRequestRepo.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Leave not found"));

        // Security check
        if (!leave.getEmployee().getId().equals(employee.getId())) {
            throw new RuntimeException("Unauthorized action");
        }

        if ("PENDING".equals(leave.getStatus())) {
            leave.setStatus("CANCELLED");
            leaveRequestRepo.save(leave);
        }

        return "redirect:/employee/leave-status";
    }

    // ================= LEAVE HISTORY (NO PENDING) =================
    @GetMapping("/leave-history")
    public String leaveHistory(Model model) {

        Employee employee = getEmployee();

        List<LeaveRequest> allLeaves = employee.getLeaveRequests();

        List<LeaveRequest> approved = allLeaves.stream()
                .filter(l -> "APPROVED".equals(l.getStatus()))
                .toList();

        List<LeaveRequest> rejected = allLeaves.stream()
                .filter(l -> "REJECTED".equals(l.getStatus()))
                .toList();

        List<LeaveRequest> cancelled = allLeaves.stream()
                .filter(l -> "CANCELLED".equals(l.getStatus()))
                .toList();

        model.addAttribute("employee", employee);
        model.addAttribute("approvedLeaves", approved);
        model.addAttribute("rejectedLeaves", rejected);
        model.addAttribute("cancelledLeaves", cancelled);
        model.addAttribute("view", "leave-history");

        return "employee/leave_history";
    }

    // ================= LEAVE BALANCE =================
    @GetMapping("/leave-balance")
    public String leaveBalance(Model model) {

        Employee employee = getEmployee();

        List<LeaveBalance> balances =
                leaveBalanceRepo.findByEmployee_Id(employee.getId());

        model.addAttribute("employee", employee);
        model.addAttribute("balances", balances);
        model.addAttribute("view", "leave-balance");

        return "employee/leave_balance";
    }

    // ================= ANNOUNCEMENTS =================
    @GetMapping("/announcements")
    public String announcements(Model model) {

        Employee employee = getEmployee();

        model.addAttribute("employee", employee);
        model.addAttribute("view", "announcements");

        return "employee/announcements";
    }
}