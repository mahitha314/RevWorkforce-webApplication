package com.revworkforce.adminserviceImplTest;

import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.adminserviceImpl.HolidayServiceImpl;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.model.Holiday;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.HolidayRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HolidayServiceImplTest {

	@InjectMocks
	private HolidayServiceImpl service;

	@Mock
	private HolidayRepository holidayRepository;
	@Mock
	private NotificationService notificationService;
	@Mock
	private ActivityLogService activityLogService;
	@Mock
	private HttpServletRequest request;

	private Holiday holiday;

	@BeforeEach
	public void setup() {
		holiday = new Holiday();
		holiday.setId(1L);
		holiday.setHolidayName("Diwali");
		holiday.setHolidayDate(LocalDate.of(2026, 11, 12));
		holiday.setDescription("Festival of lights");
	}

	@Test
	public void saveHoliday_Positive() {

		when(holidayRepository.existsByHolidayDate(any(LocalDate.class))).thenReturn(false);

		when(holidayRepository.save(any(Holiday.class))).thenReturn(holiday);

		Holiday result = service.saveHoliday(holiday);

		assertNotNull(result);
		assertEquals("Diwali", result.getHolidayName());

		verify(holidayRepository).save(any(Holiday.class));
		verify(notificationService).createNotificationForAll(any(NotificationDTO.class));

		verify(activityLogService).log(anyString(), anyString(), anyString(), anyString(),
				any(HttpServletRequest.class));
	}

	@Test
	public void saveHoliday_DuplicateDate() {

		when(holidayRepository.existsByHolidayDate(any(LocalDate.class))).thenReturn(true);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			service.saveHoliday(holiday);
		});

		assertEquals("Holiday already exists for this date.", exception.getMessage());

		verify(holidayRepository, never()).save(any());
		verify(notificationService, never()).createNotificationForAll(any());
	}

	@Test
	public void updateHoliday_Positive() {

		Holiday existing = new Holiday();
		existing.setId(1L);
		existing.setHolidayName("Diwali");
		existing.setHolidayDate(LocalDate.of(2026, 11, 12));
		existing.setDescription("Old Desc");

		Holiday updatedInput = new Holiday();
		updatedInput.setHolidayName("New Diwali");
		updatedInput.setHolidayDate(LocalDate.of(2026, 11, 12));
		updatedInput.setDescription("New Desc");

		when(holidayRepository.findById(1L)).thenReturn(Optional.of(existing));

		when(holidayRepository.save(any(Holiday.class))).thenReturn(existing);

		Holiday result = service.updateHoliday(1L, updatedInput);

		assertNotNull(result);
		assertEquals("New Diwali", result.getHolidayName());

		verify(activityLogService).log(anyString(), anyString(), anyString(), anyString(),
				any(HttpServletRequest.class));
	}

	@Test
	public void updateHoliday_NotFound() {

		when(holidayRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> {
			service.updateHoliday(1L, holiday);
		});
	}

	@Test
	public void deleteHoliday_Positive() {

		when(holidayRepository.findById(1L)).thenReturn(Optional.of(holiday));

		service.deleteHoliday(1L);

		verify(holidayRepository).delete(holiday);
		verify(activityLogService).log(anyString(), anyString(), anyString(), anyString(),
				any(HttpServletRequest.class));
	}

	@Test
	public void getAllHolidays_Positive() {

		when(holidayRepository.findAllByOrderByHolidayDateAsc()).thenReturn(List.of(holiday));

		List<Holiday> list = service.getAllHolidays();

		assertEquals(1, list.size());
	}

	@Test
	public void getHolidayById_Positive() {

		when(holidayRepository.findById(1L)).thenReturn(Optional.of(holiday));

		Holiday result = service.getHolidayById(1L);

		assertNotNull(result);
	}

	@Test
	public void getHolidayById_NotFound() {

		when(holidayRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> {
			service.getHolidayById(1L);
		});
	}

}