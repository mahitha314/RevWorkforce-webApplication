package com.revworkforce.admincontroller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.revworkforce.adminservice.AnnouncementService;
import com.revworkforce.model.Announcement;


@Controller
@RequestMapping("/admin/announcement")
public class AnnouncementController {

    private final AnnouncementService service;

    public AnnouncementController(AnnouncementService service) {
        this.service = service;
    }

    @GetMapping
    public String announcementPage(Model model) {
    	 model.addAttribute("view", "announcement");
        model.addAttribute("announcement", new Announcement());
        model.addAttribute("announcements", service.getAllAnnouncements());
        return "admin/announcements";
    }

    @PostMapping("/save")
    public String saveAnnouncement(@ModelAttribute Announcement announcement) {
        service.saveAnnouncement(announcement);
        return "redirect:/admin/announcement";
    }

    @GetMapping("/edit/{id}")
    public String editAnnouncement(@PathVariable Long id, Model model) {
    	
    	model.addAttribute("view", "announcement"); 
        model.addAttribute("announcement", service.getAnnouncementById(id));
        model.addAttribute("announcements", service.getAllAnnouncements());
        return "admin/announcements";
    }

    @PostMapping("/update/{id}")
    public String updateAnnouncement(@PathVariable Long id,
                                     @ModelAttribute Announcement announcement) {
        service.updateAnnouncement(id, announcement);
        return "redirect:/admin/announcement";
    }

    @GetMapping("/delete/{id}")
    public String deleteAnnouncement(@PathVariable Long id) {
        service.deleteAnnouncement(id);
        return "redirect:/admin/announcement";
    }
}