package com.revworkforce.admincontroller;

import com.revworkforce.adminservice.AdminService;
import com.revworkforce.adminservice.EmployeeManagementService;
import com.revworkforce.adminservice.HolidayService;
import com.revworkforce.adminservice.LeaveManagementService;
import com.revworkforce.adminservice.SystemConfigService;
import com.revworkforce.dto.LeaveBalanceDTO;
import com.revworkforce.dto.LeaveTypeDTO;
import com.revworkforce.model.Department;
import com.revworkforce.model.Designation;
import com.revworkforce.model.Employee;
import com.revworkforce.model.SystemActivityLog;
//import com.revworkforce.model.Holiday;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.SystemActivityLogRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")   // ✅ Added base mapping
public class AdminController {

    private final AdminService adminService;
    private final LeaveManagementService leaveService;
    private final EmployeeRepository employeeRepository;
    private final HolidayService holidayService;
    private final SystemConfigService systemConfigService;
    private final EmployeeManagementService employeeManagementService;
private final SystemActivityLogRepository logRepository;
//    private final LeaveManagementService leaveService;
    
    public AdminController(AdminService adminService,
                           LeaveManagementService leaveService,
                           EmployeeRepository employeeRepository,
                           HolidayService holidayService,
                           SystemConfigService systemConfigService,
                           SystemActivityLogRepository logRepository,
                           EmployeeManagementService employeeManagementService
    			) {

        this.adminService = adminService;
        this.leaveService = leaveService;
        this.employeeRepository = employeeRepository;
        this.holidayService = holidayService;
        this.systemConfigService=systemConfigService;
        this.logRepository=logRepository;
        this.employeeManagementService=employeeManagementService;
        
    }


    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String adminDashboard(Authentication authentication, Model model) {

        model.addAttribute("view", "dashboard");

        String email = authentication.getName();

        Employee employee = adminService.getEmployeeByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("employee", employee);

        model.addAttribute("totalEmployees",
                employeeManagementService.countEmployees());

        model.addAttribute("totalManagers",
                employeeManagementService.countManagers());

        model.addAttribute("totalDepartments",
                systemConfigService.countDepartments());

        model.addAttribute("totalDesignations",
                systemConfigService.countDesignations());

        model.addAttribute("totalLeaves",
                leaveService.countLeaves());

        return "admin/admin-dashboard";
    }	
    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {

        model.addAttribute("view", "profile");

        String email = authentication.getName();

        Employee employee = adminService.getEmployeeByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("employee", employee);

        return "admin/profile";
    }
    
    @GetMapping("/employee-management")
    public String employeeManagementPage(Model model) {
        model.addAttribute("view", "employee");
        return "admin/employee-management";
    }

    // ================= LEAVE MANAGEMENT PAGE =================
    @GetMapping("/leave-management")
    public String leavePage(Model model) {

        model.addAttribute("view", "leave");   // ✅ ADD THIS
        model.addAttribute("leaveTypes", leaveService.getAllLeaveTypes());
        model.addAttribute("employees", employeeRepository.findAll());

        return "admin/leave-management";
    }

    // ================= CREATE LEAVE TYPE =================
    @PostMapping("/leave/type")
    public String createLeaveType(@RequestParam String typeName,
                                  @RequestParam Integer totalDays) {

        LeaveTypeDTO dto = new LeaveTypeDTO(null, typeName, totalDays);
        leaveService.createLeaveType(dto);

        return "redirect:/admin/leave-management";   // ✅ Correct redirect
    }

    // ================= ASSIGN LEAVE =================
    @PostMapping("/leave/assign")
    public String assignLeave(@RequestParam Long employeeId,
                              @RequestParam Long leaveTypeId,
                              @RequestParam int totalDays) {

        leaveService.assignLeaveToEmployee(employeeId, leaveTypeId, totalDays);

        return "redirect:/admin/leave-management";
    }

    // ================= ADJUST LEAVE =================
    @PostMapping("/leave/adjust")
    public String adjustLeave(@RequestParam Long employeeId,
                              @RequestParam Long leaveTypeId,
                              @RequestParam Integer days,
                              @RequestParam String reason) {

        LeaveBalanceDTO dto = new LeaveBalanceDTO();
        dto.setEmployeeId(employeeId);
        dto.setLeaveTypeId(leaveTypeId);
        dto.setDays(days);
        dto.setReason(reason);

        leaveService.adjustLeave(dto);

        return "redirect:/admin/leave-management";
    }

    // ================= EMPLOYEE REPORT =================
    @GetMapping("/leave/employee-view")
    public String viewEmployeeLeave(@RequestParam Long employeeId, Model model) {

        model.addAttribute("leaveBalances",
                leaveService.getEmployeeLeaveInfo(employeeId));
        model.addAttribute("leaveTypes", leaveService.getAllLeaveTypes());
        model.addAttribute("employees", employeeRepository.findAll());

        return "admin/leave-management";
    }

    // ================= DEPARTMENT REPORT =================
    @GetMapping("/leave/department-view")
    public String departmentReport(@RequestParam Long departmentId, Model model) {

        model.addAttribute("leaveBalances",
                leaveService.getDepartmentLeaveReport(departmentId));
        model.addAttribute("leaveTypes", leaveService.getAllLeaveTypes());
        model.addAttribute("employees", employeeRepository.findAll());

        return "admin/leave-management";
    }
 // ================= DEPARTMENT PAGE =================
    @GetMapping("/system-config")
    public String systemConfig(@RequestParam(required = false) Long deptId, Model model) {

        model.addAttribute("view", "system-config");  // ✅ add this

        model.addAttribute("departments", systemConfigService.getAllDepartments());
        model.addAttribute("department", new Department());
        model.addAttribute("designation", new Designation());

        if (deptId != null) {
            Department selected = systemConfigService.getDepartmentById(deptId);
            model.addAttribute("selectedDept", selected);
            model.addAttribute("designations",
                    systemConfigService.getDesignationsByDepartmentId(deptId));
        }

        return "admin/system-config";
    }
    @PostMapping("/departments/save")
    public String saveDepartment(@ModelAttribute Department department,
                                 RedirectAttributes redirectAttributes) {

        if (department.getId() == null) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Department added successfully!");
        } else {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Department updated successfully!");
        }

        systemConfigService.saveDepartment(department);

        return "redirect:/admin/system-config";
    }
    @GetMapping("/departments/delete/{id}")
    public String deleteDepartment(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {

        systemConfigService.deleteDepartment(id);

        redirectAttributes.addFlashAttribute("successMessage",
                "Department deleted successfully!");

        return "redirect:/admin/system-config";
    }
    @PostMapping("/departments/designation/save/{deptId}")
    public String saveDesignation(@PathVariable Long deptId,
                                  @ModelAttribute Designation designation,
                                  RedirectAttributes redirectAttributes) {

        if (designation.getId() == null) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Designation added successfully!");
        } else {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Designation updated successfully!");
        }

        systemConfigService.saveDesignation(deptId, designation);

        return "redirect:/admin/system-config?deptId=" + deptId;
    }
    @GetMapping("/departments/designation/delete/{id}/{deptId}")
    public String deleteDesignation(@PathVariable Long id,
                                    @PathVariable Long deptId,
                                    RedirectAttributes redirectAttributes) {

        systemConfigService.deleteDesignation(id);

        redirectAttributes.addFlashAttribute("successMessage",
                "Designation deleted successfully!");

        return "redirect:/admin/system-config?deptId=" + deptId;
    }
    
    

    // ✅ View All Logs
    @GetMapping("/logs")
    public String viewAllLogs(Model model) {
    	model.addAttribute("view", "logs");
        List<SystemActivityLog> logs =
                logRepository.findAllByOrderByCreatedAtDesc();

        model.addAttribute("logs", logs);

        return "admin/activity-logs";
    }

    // ✅ Search by Username
    @GetMapping("/logs/search")
    public String searchLogs(@RequestParam("keyword") String keyword,
                             Model model) {

        List<SystemActivityLog> logs =
                logRepository
                    .findByUserNameContainingIgnoreCase(keyword);

        model.addAttribute("logs", logs);

        return "admin/activity-logs";
    }

    // ✅ Filter by Date Range
    @GetMapping("/logs/filter")
    public String filterByDate(
            @RequestParam("from") String fromDate,
            @RequestParam("to") String toDate,
            Model model) {

        LocalDateTime start =
                LocalDate.parse(fromDate).atStartOfDay();

        LocalDateTime end =
                LocalDate.parse(toDate).atTime(23,59,59);

        List<SystemActivityLog> logs =
                logRepository.findByCreatedAtBetween(start, end);

        model.addAttribute("logs", logs);

        return "admin/activity-logs";
    }
    
   
    

}