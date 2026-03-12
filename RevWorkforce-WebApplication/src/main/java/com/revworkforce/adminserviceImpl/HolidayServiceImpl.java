package com.revworkforce.adminserviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.adminservice.HolidayService;
import com.revworkforce.dto.NotificationDTO;
import com.revworkforce.model.Holiday;
import com.revworkforce.notification.NotificationService;
import com.revworkforce.repository.HolidayRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class HolidayServiceImpl implements HolidayService {

	private final HolidayRepository holidayRepository;
	private final NotificationService notificationService;
	private final ActivityLogService activityLogService;
	private final HttpServletRequest request;

	public HolidayServiceImpl(HolidayRepository holidayRepository, NotificationService notificationService,
			ActivityLogService activityLogService, HttpServletRequest request) {
		this.holidayRepository = holidayRepository;
		this.notificationService = notificationService;
		this.activityLogService = activityLogService;
		this.request = request;
	}

	@Override
	public Holiday saveHoliday(Holiday holiday) {

		if (holidayRepository.existsByHolidayDate(holiday.getHolidayDate())) {
			throw new RuntimeException("Holiday already exists for this date.");
		}

		Holiday saved = holidayRepository.save(holiday);

		NotificationDTO dto = new NotificationDTO();
		dto.setTitle("New Holiday Added");
		dto.setMessage("Holiday: " + holiday.getHolidayName() + " on " + holiday.getHolidayDate());
		dto.setType("HOLIDAY");
		dto.setStatus("ACTIVE");

		notificationService.createNotificationForAll(dto);

		activityLogService.log("Holiday Added", "Holiday Management", "Added holiday " + holiday.getHolidayName(),
				"SUCCESS", request);

		return saved;
	}

	@Override
	public Holiday updateHoliday(Long id, Holiday holiday) {

		Holiday existing = holidayRepository.findById(id).orElseThrow(() -> new RuntimeException("Holiday not found"));

		if (!existing.getHolidayDate().equals(holiday.getHolidayDate())
				&& holidayRepository.existsByHolidayDate(holiday.getHolidayDate())) {
			throw new RuntimeException("Another holiday already exists on this date.");
		}

		existing.setHolidayName(holiday.getHolidayName());
		existing.setHolidayDate(holiday.getHolidayDate());
		existing.setDescription(holiday.getDescription());

		Holiday updated = holidayRepository.save(existing);

		activityLogService.log("Holiday Updated", "Holiday Management", "Updated holiday " + updated.getHolidayName(),
				"SUCCESS", request);

		return updated;
	}

	@Override
	public void deleteHoliday(Long id) {
		Holiday holiday = holidayRepository.findById(id).orElseThrow(() -> new RuntimeException("Holiday not found"));

		holidayRepository.delete(holiday);

		activityLogService.log("Holiday Deleted", "Holiday Management", "Deleted holiday " + holiday.getHolidayName(),
				"SUCCESS", request);
	}

	@Override
	public List<Holiday> getAllHolidays() {
		return holidayRepository.findAllByOrderByHolidayDateAsc();
	}

	@Override
	public Holiday getHolidayById(Long id) {
		return holidayRepository.findById(id).orElseThrow(() -> new RuntimeException("Holiday not found"));
	}
}