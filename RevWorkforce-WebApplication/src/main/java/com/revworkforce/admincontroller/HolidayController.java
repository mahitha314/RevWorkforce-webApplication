package com.revworkforce.admincontroller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.revworkforce.adminservice.AdminService;
import com.revworkforce.adminservice.HolidayService;
import com.revworkforce.model.Employee;
import com.revworkforce.model.Holiday;

@Controller
@RequestMapping("/admin/holiday")
public class HolidayController {

	private final HolidayService holidayService;
	private final AdminService adminService;

	public HolidayController(HolidayService holidayService, AdminService adminService) {
		this.holidayService = holidayService;
		this.adminService = adminService;
	}

	@GetMapping
	public String viewHolidays(Authentication authentication, Model model) {

		String email = authentication.getName();

		Employee employee = adminService.getEmployeeByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found"));

		model.addAttribute("employee", employee);

		model.addAttribute("view", "holiday");

		model.addAttribute("holidays", holidayService.getAllHolidays());
		model.addAttribute("holiday", new Holiday());

		return "admin/holiday-management";
	}

	@PostMapping("/save")
	public String saveHoliday(@ModelAttribute Holiday holiday) {
		holidayService.saveHoliday(holiday);
		return "redirect:/admin/holiday?success=added";
	}

	@GetMapping("/edit/{id}")
	public String editHoliday(@PathVariable Long id, Model model) {
		Holiday holiday = holidayService.getHolidayById(id);
		model.addAttribute("holiday", holiday);
		model.addAttribute("holidays", holidayService.getAllHolidays());
		return "admin/holiday-management";
	}

	@PostMapping("/update/{id}")
	public String updateHoliday(@PathVariable Long id, @ModelAttribute Holiday holiday) {
		holidayService.updateHoliday(id, holiday);
		return "redirect:/admin/holiday?success=updated";
	}

	@GetMapping("/delete/{id}")
	public String deleteHoliday(@PathVariable Long id) {
		holidayService.deleteHoliday(id);
		return "redirect:/admin/holiday?success=deleted";
	}

}