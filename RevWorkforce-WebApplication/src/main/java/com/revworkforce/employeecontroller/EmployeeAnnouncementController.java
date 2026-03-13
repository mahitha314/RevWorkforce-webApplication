package com.revworkforce.employeecontroller;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.employeeservice.AnnouncementService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@RestController
@RequestMapping("/api/employee")
public class EmployeeAnnouncementController {

    private final AnnouncementService announcementService;

    public EmployeeAnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping("/announcements")
    public ResponseEntity<ApiResponse> getAnnouncements() {

        ApiResponse response = announcementService.getAllAnnouncements();

        return ResponseEntity.ok(response);
    }
}