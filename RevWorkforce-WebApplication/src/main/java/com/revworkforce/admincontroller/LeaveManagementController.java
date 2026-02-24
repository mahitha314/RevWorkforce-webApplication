package com.revworkforce.admincontroller;

<<<<<<< HEAD
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.revworkforce.adminservice.LeaveManagementService;
import com.revworkforce.model.Department;
import com.revworkforce.model.Designation;
import com.revworkforce.model.Employee;
import com.revworkforce.model.Holiday;
import com.revworkforce.repository.EmployeeRepository;


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

    @GetMapping
    public String leaveDashboard(Model model) {

        model.addAttribute("page", "leave");
        model.addAttribute("leaveRequests", leaveService.getAllLeaveRequests());
        model.addAttribute("pendingCount", leaveService.getPendingCount());
        model.addAttribute("approvedCount", leaveService.getApprovedCount());
        model.addAttribute("rejectedCount", leaveService.getRejectedCount());
        model.addAttribute("holiday", new Holiday());
        model.addAttribute("holidays", leaveService.getAllHolidays());

        return "admin/employee_managementt";
    }
    @GetMapping("/types")
    public String configureLeaveTypes(Model model) {
        model.addAttribute("page", "leaveTypes");
        return "admin/employee_managementt";
    }
    
    @GetMapping("/calendar")
    public String holidayCalendar(Model model) {

        model.addAttribute("page", "holidayCalendar");
        model.addAttribute("holiday", new Holiday());
        model.addAttribute("holidays", leaveService.getAllHolidays());

        return "admin/employee_managementt";
    }
    
    @GetMapping("/approve/{id}")
    public String approveLeave(@PathVariable Long id) {
        leaveService.approveLeave(id);
        return "redirect:/admin/leaves";
    }

    @GetMapping("/reject/{id}")
    public String rejectLeave(@PathVariable Long id) {
        leaveService.rejectLeave(id);
        return "redirect:/admin/leaves";
    }

    @PostMapping("/holiday/add")
    public String addHoliday(@ModelAttribute Holiday holiday) {
        leaveService.addHoliday(holiday);
        return "redirect:/admin/leaves/calendar";   
    }

    @GetMapping("/holiday/delete/{id}")
    public String deleteHoliday(@PathVariable Long id) {
        leaveService.deleteHoliday(id);
        return "redirect:/admin/leaves/calendar";  
    }

    @GetMapping("/quotas")
    public String assignLeaveQuotas(Model model) {

        model.addAttribute("page", "leaveQuotas");
        model.addAttribute("employees", employeeRepository.findAll());

        return "admin/employee_managementt";
    }

    @PostMapping("/quotas/assign")
    public String assignQuota(@RequestParam Long empId,
                              @RequestParam String leaveType,
                              @RequestParam int totalDays) {

        leaveService.assignLeaveQuota(empId, leaveType, totalDays);
        return "redirect:/admin/leaves/quotas";
    }
    
    @GetMapping("/balances")
    public String adjustBalances(Model model) {

        model.addAttribute("page", "leaveBalances");
        model.addAttribute("balances", leaveService.getAllBalances());

        return "admin/employee_managementt";
    }


    @GetMapping("/performance")
    public String performanceReviews(Model model) {

        model.addAttribute("page", "performance");
        model.addAttribute("reviews",
                leaveService.getAllPerformanceReviews());

        return "admin/employee_managementt";
    }

    @PostMapping("/performance/review")
    public String reviewPerformance(@RequestParam Long reviewId,
                                    @RequestParam int managerRating,
                                    @RequestParam String managerFeedback) {

        leaveService.reviewPerformance(reviewId, managerRating, managerFeedback);

        return "redirect:/admin/leaves/performance";
    }


    @GetMapping("/team-goals")
    public String teamGoals(Model model) {

        model.addAttribute("page", "teamGoals");
        model.addAttribute("goals",
                leaveService.getAllTeamGoals());

        return "admin/employee_managementt";
    }

    @GetMapping("/team-goals/view/{id}")
    public String viewGoal(@PathVariable Long id,
                           Model model) {

        model.addAttribute("page", "viewGoal");
        model.addAttribute("goal",
                leaveService.getGoalById(id));

        return "admin/employee_managementt";
    }

    @GetMapping("/departments")
    public String departments(Model model) {

        model.addAttribute("page", "departments");
        model.addAttribute("department", new Department());
        model.addAttribute("designation", new Designation());

        model.addAttribute("departments", leaveService.getAllDepartments());
        model.addAttribute("designations", leaveService.getAllDesignations());

        model.addAttribute("designationCount",
                leaveService.getDesignationCount());

        return "admin/employee_managementt";
    }


    @PostMapping("/departments/add")
    public String addDepartment(@ModelAttribute Department department) {

        leaveService.addDepartment(department);
        return "redirect:/admin/leaves/departments";
    }


    @GetMapping("/departments/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {

        leaveService.deleteDepartment(id);
        return "redirect:/admin/leaves/departments";
    }

    @GetMapping("/departments/edit/{id}")
    public String editDepartment(@PathVariable Long id, Model model) {

        model.addAttribute("page", "departments");

        model.addAttribute("department",
                leaveService.getDepartmentById(id));

        model.addAttribute("designation", new Designation());

        model.addAttribute("departments",
                leaveService.getAllDepartments());

        model.addAttribute("designations",
                leaveService.getAllDesignations());

        model.addAttribute("designationCount",
                leaveService.getDesignationCount());

        return "admin/employee_managementt";
    }


    @PostMapping("/departments/update")
    public String updateDepartment(@ModelAttribute Department department) {

        leaveService.updateDepartment(department);
        return "redirect:/admin/leaves/departments";
    }

    @PostMapping("/designations/add")
    public String addDesignation(@ModelAttribute Designation designation) {

        leaveService.addDesignation(designation);
        return "redirect:/admin/leaves/departments";
    }

    @GetMapping("/designations/edit/{id}")
    public String editDesignation(@PathVariable Long id, Model model) {

        model.addAttribute("page", "departments");

        model.addAttribute("designation",
                leaveService.getDesignationById(id));

        model.addAttribute("department", new Department());

        model.addAttribute("departments",
                leaveService.getAllDepartments());

        model.addAttribute("designations",
                leaveService.getAllDesignations());

        model.addAttribute("designationCount",
                leaveService.getDesignationCount());

        return "admin/employee_managementt";   
    }

    @PostMapping("/designations/update")
    public String updateDesignation(@ModelAttribute Designation designation) {

        leaveService.saveDesignation(designation);

        return "redirect:/admin/leaves/departments";  
    }


    @GetMapping("/designations/delete/{id}")
    public String deleteDesignation(@PathVariable Long id) {

        leaveService.deleteDesignation(id);
        return "redirect:/admin/leaves/departments";
    }

    @GetMapping("/reports")
    public String reports(Model model) {

        model.addAttribute("page", "reports");

        model.addAttribute("employees",
                employeeRepository.findAll());

        model.addAttribute("leaveRequests",
                leaveService.getAllLeaveRequests());

        model.addAttribute("departments",
                leaveService.getAllDepartments());

        return "admin/employee_managementt";
    }
    @GetMapping("/activity-logs")
    public String systemActivityLogs(Model model) {

        model.addAttribute("page", "activityLogs");
        model.addAttribute("logs",
                leaveService.getSystemActivityLogs());

        return "admin/employee_managementt";
    }
    @GetMapping("/notifications")
    public String notifications(Model model) {

        model.addAttribute("page", "notifications");
        model.addAttribute("notifications",
                leaveService.getAdminNotifications());

        return "admin/employee_managementt";
    }
}
=======
public class LeaveManagementController {

}
>>>>>>> dev
