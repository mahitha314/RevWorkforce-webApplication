package com.revworkforce.admincontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.revworkforce.adminservice.HolidayService;
import com.revworkforce.model.Holiday;

@Controller
@RequestMapping("/admin/holiday")
public class HolidayController {

	private final HolidayService holidayService;

	public HolidayController(HolidayService holidayService) {
		this.holidayService = holidayService;
	}

	@GetMapping
	public String viewHolidays(Model model) {

		model.addAttribute("view", "holiday"); 

		model.addAttribute("holidays", holidayService.getAllHolidays());
		model.addAttribute("holiday", new Holiday());

		return "admin/holiday-management";
	}

	@PostMapping("/save")
	public String saveHoliday(@ModelAttribute Holiday holiday, RedirectAttributes redirectAttributes) {

		holidayService.saveHoliday(holiday);

		redirectAttributes.addFlashAttribute("successMessage", "Holiday added successfully!");

		return "redirect:/admin/holiday";
	}

	@GetMapping("/edit/{id}")
	public String editHoliday(@PathVariable Long id, Model model) {

		model.addAttribute("view", "holiday");

		Holiday holiday = holidayService.getHolidayById(id);

		model.addAttribute("holiday", holiday);
		model.addAttribute("holidays", holidayService.getAllHolidays());

		return "admin/holiday-management";
	}

	@PostMapping("/update/{id}")
	public String updateHoliday(@PathVariable Long id, @ModelAttribute Holiday holiday,
			RedirectAttributes redirectAttributes) {

		holidayService.updateHoliday(id, holiday);

		redirectAttributes.addFlashAttribute("successMessage", "Holiday updated successfully!");

		return "redirect:/admin/holiday";
	}

	@GetMapping("/delete/{id}")
	public String deleteHoliday(@PathVariable Long id, RedirectAttributes redirectAttributes) {

		holidayService.deleteHoliday(id);

		redirectAttributes.addFlashAttribute("successMessage", "Holiday deleted successfully!");

		return "redirect:/admin/holiday";
	}
}