package com.revworkforce.employeeserviceImpl;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.model.Announcement;
import com.revworkforce.repository.AnnouncementRepository;
import com.revworkforce.employeeservice.AnnouncementService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeAnnouncementServiceImpl implements AnnouncementService {

	private final AnnouncementRepository announcementRepo;

	public EmployeeAnnouncementServiceImpl(AnnouncementRepository announcementRepo) {
		this.announcementRepo = announcementRepo;
	}

	@Override
	public ApiResponse getAllAnnouncements() {

		List<Announcement> announcements = announcementRepo.findAll();

		return new ApiResponse(200, "Announcements fetched successfully", announcements);
	}
}