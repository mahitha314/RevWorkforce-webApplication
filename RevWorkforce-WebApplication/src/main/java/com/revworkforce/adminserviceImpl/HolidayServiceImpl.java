package com.revworkforce.adminserviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger =
            LoggerFactory.getLogger(HolidayServiceImpl.class);

    private final HolidayRepository holidayRepository;
    private final NotificationService notificationService;
    private final ActivityLogService activityLogService;
    private final HttpServletRequest request;

    public HolidayServiceImpl(HolidayRepository holidayRepository,
                              NotificationService notificationService,
                              ActivityLogService activityLogService,
                              HttpServletRequest request) {
        this.holidayRepository = holidayRepository;
        this.notificationService = notificationService;
        this.activityLogService = activityLogService;
        this.request = request;

        logger.info("HolidayServiceImpl initialized");
    }

    @Override
    public Holiday saveHoliday(Holiday holiday) {

        logger.info("Attempting to create holiday: {}", holiday.getHolidayName());

        if (holidayRepository.existsByHolidayDate(holiday.getHolidayDate())) {
            logger.warn("Holiday already exists on date: {}", holiday.getHolidayDate());
            throw new RuntimeException("Holiday already exists for this date.");
        }

        Holiday saved = holidayRepository.save(holiday);

        logger.debug("Holiday saved with ID: {}", saved.getId());

        NotificationDTO dto = new NotificationDTO();
        dto.setTitle("New Holiday Added");
        dto.setMessage("Holiday: " + holiday.getHolidayName() + " on " + holiday.getHolidayDate());
        dto.setType("HOLIDAY");
        dto.setStatus("ACTIVE");

        logger.info("Sending holiday notification to all employees");

        notificationService.createNotificationForAll(dto);

        activityLogService.log(
                "Holiday Added",
                "Holiday Management",
                "Added holiday " + holiday.getHolidayName(),
                "SUCCESS",
                request
        );

        logger.info("Holiday created successfully: {}", holiday.getHolidayName());

        return saved;
    }

    @Override
    public Holiday updateHoliday(Long id, Holiday holiday) {

        logger.info("Updating holiday with ID: {}", id);

        Holiday existing = holidayRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Holiday not found with ID: {}", id);
                    return new RuntimeException("Holiday not found");
                });

        if (!existing.getHolidayDate().equals(holiday.getHolidayDate())
                && holidayRepository.existsByHolidayDate(holiday.getHolidayDate())) {

            logger.warn("Duplicate holiday date detected: {}", holiday.getHolidayDate());
            throw new RuntimeException("Another holiday already exists on this date.");
        }

        existing.setHolidayName(holiday.getHolidayName());
        existing.setHolidayDate(holiday.getHolidayDate());
        existing.setDescription(holiday.getDescription());

        Holiday updated = holidayRepository.save(existing);

        logger.info("Holiday updated successfully: {}", updated.getHolidayName());

        activityLogService.log(
                "Holiday Updated",
                "Holiday Management",
                "Updated holiday " + updated.getHolidayName(),
                "SUCCESS",
                request
        );

        return updated;
    }

    @Override
    public void deleteHoliday(Long id) {

        logger.warn("Deleting holiday with ID: {}", id);

        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Holiday not found with ID: {}", id);
                    return new RuntimeException("Holiday not found");
                });

        holidayRepository.delete(holiday);

        logger.info("Holiday deleted successfully: {}", holiday.getHolidayName());

        activityLogService.log(
                "Holiday Deleted",
                "Holiday Management",
                "Deleted holiday " + holiday.getHolidayName(),
                "SUCCESS",
                request
        );
    }

    @Override
    public List<Holiday> getAllHolidays() {

        logger.info("Fetching all holidays");

        List<Holiday> holidays = holidayRepository.findAllByOrderByHolidayDateAsc();

        logger.debug("Total holidays fetched: {}", holidays.size());

        return holidays;
    }

    @Override
    public Holiday getHolidayById(Long id) {

        logger.info("Fetching holiday with ID: {}", id);

        return holidayRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Holiday not found with ID: {}", id);
                    return new RuntimeException("Holiday not found");
                });
    }
}