package com.revworkforce.admincontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.revworkforce.adminservice.HolidayService;
import com.revworkforce.model.Holiday;

@Controller
@RequestMapping("/admin/holiday")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    // ✅ View Page
    @GetMapping
    public String viewHolidays(Model model) {
        model.addAttribute("holidays", holidayService.getAllHolidays());
        model.addAttribute("holiday", new Holiday()); // empty form
        return "admin/holiday-management";
    }

    // ✅ SAVE
    @PostMapping("/save")
    public String saveHoliday(@ModelAttribute Holiday holiday) {
        holidayService.saveHoliday(holiday);
        return "redirect:/admin/system-config?success=added";
    }

    // ✅ EDIT (LOAD DATA INTO FORM)
    @GetMapping("/edit/{id}")
    public String editHoliday(@PathVariable Long id, Model model) {
        Holiday holiday = holidayService.getHolidayById(id);
        model.addAttribute("holiday", holiday);
        model.addAttribute("holidays", holidayService.getAllHolidays());
        return "admin/holiday-management";
    }

    // ✅ UPDATE
    @PostMapping("/update/{id}")
    public String updateHoliday(@PathVariable Long id,
                                @ModelAttribute Holiday holiday) {
        holidayService.updateHoliday(id, holiday);
        return "redirect:/admin/system-config?success=updated";
    }

    // ✅ DELETE
    @GetMapping("/delete/{id}")
    public String deleteHoliday(@PathVariable Long id) {
        holidayService.deleteHoliday(id);
        return "redirect:/admin/system-config?success=deleted";
    }
}