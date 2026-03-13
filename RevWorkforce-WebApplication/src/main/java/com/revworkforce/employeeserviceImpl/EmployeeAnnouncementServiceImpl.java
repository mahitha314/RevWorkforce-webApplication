package com.revworkforce.employeeserviceImpl;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.model.Announcement;
import com.revworkforce.repository.AnnouncementRepository;
import com.revworkforce.employeeservice.AnnouncementService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeAnnouncementServiceImpl implements AnnouncementService {

    private static final Logger logger =
            LoggerFactory.getLogger(EmployeeAnnouncementServiceImpl.class);

    private final AnnouncementRepository announcementRepo;

    public EmployeeAnnouncementServiceImpl(AnnouncementRepository announcementRepo) {
        this.announcementRepo = announcementRepo;
        logger.info("EmployeeAnnouncementServiceImpl initialized");
    }

    @Override
    public ApiResponse getAllAnnouncements() {

        logger.info("Fetching all announcements for employees");

        List<Announcement> announcements = announcementRepo.findAll();

        logger.debug("Total announcements fetched: {}", announcements.size());

        return new ApiResponse(
                200,
                "Announcements fetched successfully",
                announcements
        );
    }
}