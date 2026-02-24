package com.workforce.admincontroller;

import com.workforce.adminservice.AnnouncementService;
import com.workforce.model.Announcement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/announcements")
public class AnnouncementsController {

    private final AnnouncementService announcementService;

    public AnnouncementsController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public String viewAnnouncements(Model model) {
    	

        model.addAttribute("announcements",
                announcementService.getAllAnnouncements());

        model.addAttribute("announcement", new Announcement());

        return "admin/announcements";
    }

    @PostMapping("/add")
    public String addAnnouncement(@ModelAttribute Announcement announcement) {

        announcementService.addAnnouncement(announcement);

        return "redirect:/admin/announcements";
    }

    @GetMapping("/edit/{id}")
    public String editAnnouncement(@PathVariable Long id,
                                   Model model) {

        model.addAttribute("announcement",
                announcementService.getAnnouncementById(id));

        model.addAttribute("announcements",
                announcementService.getAllAnnouncements());

        return "admin/announcements";
    }

    @PostMapping("/update")
    public String updateAnnouncement(@ModelAttribute Announcement announcement) {

        announcementService.updateAnnouncement(announcement);

        return "redirect:/admin/announcements";
    }

    @GetMapping("/delete/{id}")
    public String deleteAnnouncement(@PathVariable Long id) {

        announcementService.deleteAnnouncement(id);

        return "redirect:/admin/announcements";
    }
}