package com.revworkforce.adminserviceImplTest;

import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.model.Announcement;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.adminserviceImpl.AnnouncementServiceImpl;
import com.revworkforce.repository.AnnouncementRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AnnouncementServiceImplTest {

	@InjectMocks
	private AnnouncementServiceImpl service;

	@Mock
	private AnnouncementRepository repository;

	@Mock
	private NotificationService notificationService;

	@Mock
	private ActivityLogService activityLogService;

	@Mock
	private HttpServletRequest request;

	private Announcement announcement;

	@BeforeEach
	public void setup() {
		announcement = new Announcement();
		announcement.setId(1L);
		announcement.setTitle("Holiday Notice");
		announcement.setMessage("Office closed tomorrow");
	}

	@Test
	public void saveAnnouncement_Positive() {

		when(repository.save(any(Announcement.class))).thenReturn(announcement);

		Announcement result = service.saveAnnouncement(announcement);

		assertNotNull(result);
		assertEquals("Holiday Notice", result.getTitle());
		assertEquals(LocalDate.now(), result.getPostedDate());

		verify(repository).save(any(Announcement.class));
		verify(notificationService).createNotificationForAll(any(NotificationDTO.class));
		verify(activityLogService).log(eq("Created Announcement"), eq("Announcement"), contains("Created announcement"),
				eq("SUCCESS"), eq(request));
	}

	@Test
	public void saveAnnouncement_Negative() {

		when(repository.save(any(Announcement.class))).thenThrow(new RuntimeException("DB Error"));

		assertThrows(RuntimeException.class, () -> {
			service.saveAnnouncement(announcement);
		});

		verify(notificationService, never()).createNotificationForAll(any(NotificationDTO.class));

		verify(activityLogService, never()).log(anyString(), anyString(), anyString(), anyString(),
				any(HttpServletRequest.class));
	}

	// ==============================
	// UPDATE - Positive
	// ==============================
	@Test
	public void updateAnnouncement_Positive() {

		Announcement updatedInput = new Announcement();
		updatedInput.setTitle("Updated Title");
		updatedInput.setMessage("Updated Message");

		when(repository.findById(1L)).thenReturn(Optional.of(announcement));

		when(repository.save(any())).thenReturn(announcement);

		Announcement result = service.updateAnnouncement(1L, updatedInput);

		assertEquals("Updated Title", announcement.getTitle());
		assertEquals("Updated Message", announcement.getMessage());

		verify(notificationService).createNotificationForAll(any(NotificationDTO.class));
		verify(activityLogService).log(eq("Updated Announcement"), eq("Announcement"), contains("Updated announcement"),
				eq("SUCCESS"), eq(request));
	}

	@Test
	public void updateAnnouncement_NotFound() {

		when(repository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> {
			service.updateAnnouncement(1L, announcement);
		});

		verify(repository, never()).save(any());
	}

	@Test
	public void deleteAnnouncement_Positive() {

		when(repository.findById(1L)).thenReturn(Optional.of(announcement));

		service.deleteAnnouncement(1L);

		verify(repository).delete(announcement);
		verify(activityLogService).log(eq("Deleted Announcement"), eq("Announcement"), contains("Deleted announcement"),
				eq("SUCCESS"), eq(request));
	}

	@Test
	public void deleteAnnouncement_NotFound() {

		when(repository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> {
			service.deleteAnnouncement(1L);
		});

		verify(repository, never()).delete(any());
	}

	@Test
	public void getAnnouncementById_Positive() {

		when(repository.findById(1L)).thenReturn(Optional.of(announcement));

		Announcement result = service.getAnnouncementById(1L);

		assertNotNull(result);
		assertEquals("Holiday Notice", result.getTitle());
	}

	@Test
	public void getAnnouncementById_NotFound() {

		when(repository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> {
			service.getAnnouncementById(1L);
		});
	}

	@Test
	public void getAllAnnouncements_Positive() {

		when(repository.findAllByOrderByPostedDateDesc()).thenReturn(List.of(announcement));

		List<Announcement> result = service.getAllAnnouncements();

		assertEquals(1, result.size());
		verify(repository).findAllByOrderByPostedDateDesc();
	}
}