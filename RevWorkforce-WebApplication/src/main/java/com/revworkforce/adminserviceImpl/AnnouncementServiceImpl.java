package com.revworkforce.adminserviceImpl;

import java.time.LocalDate;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.adminservice.AnnouncementService;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.model.Announcement;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.AnnouncementRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

	private static final Logger logger = LogManager.getLogger(AnnouncementServiceImpl.class);

	private final AnnouncementRepository repository;
	private final NotificationService notificationService;
	private final ActivityLogService activityLogService;
	private final HttpServletRequest request;

	public AnnouncementServiceImpl(AnnouncementRepository repository, NotificationService notificationService,
			ActivityLogService activityLogService, HttpServletRequest request) {

		this.repository = repository;
		this.notificationService = notificationService;
		this.activityLogService = activityLogService;
		this.request = request;
	}

	@Override
	public Announcement saveAnnouncement(Announcement announcement) {

		logger.info("Creating new announcement with title: {}", announcement.getTitle());

		announcement.setPostedDate(LocalDate.now());

		Announcement announced = repository.save(announcement);

		logger.debug("Announcement saved with id {}", announced.getId());

		NotificationDTO dto = new NotificationDTO();
		dto.setTitle("New Company Announcement");
		dto.setMessage(announced.getTitle() + " - " + announced.getMessage());
		dto.setType("ANNOUNCEMENT");
		dto.setStatus("ACTIVE");
		dto.setReferenceId(announced.getId());

		notificationService.createNotificationForAll(dto);

		logger.info("Notification sent for new announcement {}", announced.getTitle());

		activityLogService.log("Created Announcement", "Announcement", "Created announcement: " + announced.getTitle(),
				"SUCCESS", request);

		return announced;
	}

	@Override
	public Announcement updateAnnouncement(Long id, Announcement announcement) {

		logger.info("Updating announcement with id {}", id);

		Announcement existing = repository.findById(id).orElseThrow(() -> {
			logger.error("Announcement not found with id {}", id);
			return new RuntimeException("Announcement not found");
		});

		existing.setTitle(announcement.getTitle());
		existing.setMessage(announcement.getMessage());

		Announcement updated = repository.save(existing);

		logger.debug("Announcement updated successfully with id {}", updated.getId());

		NotificationDTO dto = new NotificationDTO();
		dto.setTitle("Announcement Updated");
		dto.setMessage("Announcement \"" + existing.getTitle() + "\" has been updated.");
		dto.setType("ANNOUNCEMENT");
		dto.setStatus("ACTIVE");
		dto.setReferenceId(updated.getId());

		notificationService.createNotificationForAll(dto);

		logger.info("Notification sent for updated announcement {}", updated.getTitle());

		activityLogService.log("Updated Announcement", "Announcement", "Updated announcement: " + updated.getTitle(),
				"SUCCESS", request);

		return updated;
	}

	@Override
	public void deleteAnnouncement(Long id) {

		logger.info("Deleting announcement with id {}", id);

		Announcement existing = repository.findById(id).orElseThrow(() -> {
			logger.error("Announcement not found with id {}", id);
			return new RuntimeException("Announcement not found");
		});

		repository.delete(existing);

		logger.info("Announcement deleted successfully: {}", existing.getTitle());

		activityLogService.log("Deleted Announcement", "Announcement", "Deleted announcement: " + existing.getTitle(),
				"SUCCESS", request);
	}

	@Override
	public Announcement getAnnouncementById(Long id) {

		logger.info("Fetching announcement with id {}", id);

		return repository.findById(id).orElseThrow(() -> {
			logger.error("Announcement not found with id {}", id);
			return new RuntimeException("Announcement not found");
		});
	}

	@Override
	public List<Announcement> getAllAnnouncements() {

		logger.info("Fetching all announcements");

		List<Announcement> announcements = repository.findAllByOrderByPostedDateDesc();

		logger.debug("Total announcements fetched: {}", announcements.size());

		return announcements;
	}
}