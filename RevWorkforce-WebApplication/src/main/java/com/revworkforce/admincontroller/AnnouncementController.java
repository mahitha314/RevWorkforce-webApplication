package com.revworkforce.admincontroller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.revworkforce.adminservice.AdminService;
import com.revworkforce.adminservice.AnnouncementService;
import com.revworkforce.model.Announcement;
import com.revworkforce.model.Employee;

@Controller
@RequestMapping("/admin/announcement")
public class AnnouncementController {

    private final AnnouncementService service;
private final AdminService adminService;
   

	public AnnouncementController(AnnouncementService service, AdminService adminService) {
		super();
		this.service = service;
		this.adminService = adminService;
	}

	@GetMapping
	public String announcementPage(Authentication authentication, Model model) {

	    model.addAttribute("view", "announcement");

	    String email = authentication.getName();

	    Employee employee = adminService.getEmployeeByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    model.addAttribute("employee", employee);

	    model.addAttribute("announcement", new Announcement());
	    model.addAttribute("announcements", service.getAllAnnouncements());

	    return "admin/announcements";
	}


    @PostMapping("/save")
    public String saveAnnouncement(@ModelAttribute Announcement announcement) {
        service.saveAnnouncement(announcement);
        return "redirect:/admin/announcement?success=added";
    }

    @GetMapping("/edit/{id}")
    public String editAnnouncement(@PathVariable Long id, Model model) {
        model.addAttribute("view", "announcement");
        model.addAttribute("announcement", service.getAnnouncementById(id));
        model.addAttribute("announcements", service.getAllAnnouncements());
        return "admin/announcements";
    }

    @PostMapping("/update/{id}")
    public String updateAnnouncement(@PathVariable Long id, @ModelAttribute Announcement announcement) {
        service.updateAnnouncement(id, announcement);
        return "redirect:/admin/announcement?success=updated";
    }

    @GetMapping("/delete/{id}")
    public String deleteAnnouncement(@PathVariable Long id) {
        service.deleteAnnouncement(id);
        return "redirect:/admin/announcement?success=deleted";
    }
}