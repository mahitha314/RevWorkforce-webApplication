package com.revworkforce.admincontroller;

import com.revworkforce.adminservice.*;
import com.revworkforce.dto.LeaveBalanceDTO;
import com.revworkforce.dto.LeaveTypeDTO;
import com.revworkforce.model.*;
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
@RequestMapping("/admin")
public class AdminController {

	private final AdminService adminService;
	private final LeaveManagementService leaveService;
	private final EmployeeRepository employeeRepository;
	private final HolidayService holidayService;
	private final SystemConfigService systemConfigService;
	private final EmployeeManagementService employeeManagementService;
	private final SystemActivityLogRepository logRepository;

	public AdminController(AdminService adminService, LeaveManagementService leaveService,
			EmployeeRepository employeeRepository, HolidayService holidayService,
			SystemConfigService systemConfigService, SystemActivityLogRepository logRepository,
			EmployeeManagementService employeeManagementService) {

		this.adminService = adminService;
		this.leaveService = leaveService;
		this.employeeRepository = employeeRepository;
		this.holidayService = holidayService;
		this.systemConfigService = systemConfigService;
		this.logRepository = logRepository;
		this.employeeManagementService = employeeManagementService;
	}
	@ModelAttribute("employee")
	public Employee loggedInEmployee(Authentication authentication) {

	    if (authentication == null)
	        return null;

	    String username = authentication.getName();

	    return employeeRepository
	            .findByEmployeeId(username)
	            .orElse(null);
	}
	@GetMapping("/dashboard")
	public String adminDashboard(Model model) {

		model.addAttribute("view", "dashboard");

		model.addAttribute("totalEmployees", employeeManagementService.countEmployees());

		model.addAttribute("totalManagers", employeeManagementService.countManagers());

		model.addAttribute("totalDepartments", systemConfigService.countDepartments());

		model.addAttribute("totalDesignations", systemConfigService.countDesignations());

		model.addAttribute("totalLeaves", leaveService.countLeaves());

		return "admin/admin-dashboard";
	}

	@GetMapping("/profile")
	public String profile(Model model) {

		model.addAttribute("view", "profile");

		return "admin/profile";
	}

	@GetMapping("/employee-management")
	public String employeeManagementPage(Model model) {

		model.addAttribute("view", "employee");

		model.addAttribute("employees", employeeRepository.findAll());
		model.addAttribute("departments", systemConfigService.getAllDepartments());
		model.addAttribute("managers", employeeManagementService.getManagers());

		return "admin/employee-management";
	}

	@GetMapping("/leave-management")
	public String leavePage(Model model) {

		model.addAttribute("view", "leave");

		model.addAttribute("leaveTypes", leaveService.getAllLeaveTypes());
		model.addAttribute("employees", employeeRepository.findAll());

		return "admin/leave-management";
	}

	@PostMapping("/leave/type")
	public String createLeaveType(@RequestParam String typeName, @RequestParam Integer totalDays) {

		LeaveTypeDTO dto = new LeaveTypeDTO(null, typeName, totalDays);
		leaveService.createLeaveType(dto);

		return "redirect:/admin/leave-management";
	}

	@PostMapping("/leave/assign")
	public String assignLeave(@RequestParam Long employeeId, @RequestParam Long leaveTypeId,
			@RequestParam int totalDays) {

		leaveService.assignLeaveToEmployee(employeeId, leaveTypeId, totalDays);

		return "redirect:/admin/leave-management";
	}

	@PostMapping("/leave/adjust")
	public String adjustLeave(@RequestParam Long employeeId, @RequestParam Long leaveTypeId, @RequestParam Integer days,
			@RequestParam String reason) {

		LeaveBalanceDTO dto = new LeaveBalanceDTO();
		dto.setEmployeeId(employeeId);
		dto.setLeaveTypeId(leaveTypeId);
		dto.setDays(days);
		dto.setReason(reason);

		leaveService.adjustLeave(dto);

		return "redirect:/admin/leave-management";
	}

	@GetMapping("/leave/employee-view")
	public String viewEmployeeLeave(@RequestParam Long employeeId, Model model) {

		model.addAttribute("view", "leave");

		model.addAttribute("leaveBalances", leaveService.getEmployeeLeaveInfo(employeeId));

		model.addAttribute("leaveTypes", leaveService.getAllLeaveTypes());

		model.addAttribute("employees", employeeRepository.findAll());

		return "admin/leave-management";
	}

	@GetMapping("/system-config")
	public String systemConfig(@RequestParam(required = false) Long deptId,
			@RequestParam(required = false) Long editDeptId, @RequestParam(required = false) Long editDesId,
			Model model) {

		model.addAttribute("view", "system-config");

		model.addAttribute("departments", systemConfigService.getAllDepartments());

		if (editDeptId != null) {
			model.addAttribute("department", systemConfigService.getDepartmentById(editDeptId));
		} else {
			model.addAttribute("department", new Department());
		}

		if (deptId != null) {

			Department selectedDept = systemConfigService.getDepartmentById(deptId);

			model.addAttribute("selectedDept", selectedDept);

			model.addAttribute("designations", systemConfigService.getDesignationsByDepartmentId(deptId));

			if (editDesId != null) {
				model.addAttribute("designation", systemConfigService.getDesignationById(editDesId));
			} else {
				model.addAttribute("designation", new Designation());
			}

		} else {
			model.addAttribute("designation", new Designation());
		}

		return "admin/system-config";
	}

	@PostMapping("/departments/save")
	public String saveDepartment(@ModelAttribute Department department, RedirectAttributes redirectAttributes) {

		if (department.getId() == null)
			redirectAttributes.addFlashAttribute("successMessage", "Department added successfully!");
		else
			redirectAttributes.addFlashAttribute("successMessage", "Department updated successfully!");

		systemConfigService.saveDepartment(department);

		return "redirect:/admin/system-config";
	}

	@GetMapping("/departments/delete/{id}")
	public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {

		systemConfigService.deleteDepartment(id);

		redirectAttributes.addFlashAttribute("successMessage", "Department deleted successfully!");

		return "redirect:/admin/system-config";
	}

	@PostMapping("/departments/designation/save/{deptId}")
	public String saveDesignation(@PathVariable Long deptId, @ModelAttribute Designation designation,
			RedirectAttributes redirectAttributes) {

		if (designation.getId() == null)
			redirectAttributes.addFlashAttribute("successMessage", "Designation added successfully!");
		else
			redirectAttributes.addFlashAttribute("successMessage", "Designation updated successfully!");

		systemConfigService.saveDesignation(deptId, designation);

		return "redirect:/admin/system-config?deptId=" + deptId;
	}

	@GetMapping("/departments/designation/delete/{id}/{deptId}")
	public String deleteDesignation(@PathVariable Long id, @PathVariable Long deptId,
			RedirectAttributes redirectAttributes) {

		systemConfigService.deleteDesignation(id);

		redirectAttributes.addFlashAttribute("successMessage", "Designation deleted successfully!");

		return "redirect:/admin/system-config?deptId=" + deptId;
	}

	@GetMapping("/logs")
	public String viewAllLogs(Model model) {

		model.addAttribute("view", "logs");

		model.addAttribute("logs", logRepository.findAllByOrderByCreatedAtDesc());

		return "admin/activity-logs";
	}

	@GetMapping("/logs/search")
	public String searchLogs(@RequestParam String keyword, Model model) {

		model.addAttribute("logs", logRepository.findByUserNameContainingIgnoreCase(keyword));

		return "admin/activity-logs";
	}

	@GetMapping("/logs/filter")
	public String filterByDate(@RequestParam String from, @RequestParam String to, Model model) {

		LocalDateTime start = LocalDate.parse(from).atStartOfDay();
		LocalDateTime end = LocalDate.parse(to).atTime(23, 59, 59);

		model.addAttribute("logs", logRepository.findByCreatedAtBetween(start, end));

		return "admin/activity-logs";
	}

}