package com.revworkforce.employeeserviceImpl;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.employeeservice.HolidayService;
import com.revworkforce.repository.HolidayRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmployeeHolidayServiceImpl implements HolidayService {

    private static final Logger logger =
            LoggerFactory.getLogger(EmployeeHolidayServiceImpl.class);

    private final HolidayRepository holidayRepository;

    public EmployeeHolidayServiceImpl(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
        logger.info("EmployeeHolidayServiceImpl initialized");
    }

    @Override
    public ApiResponse getAllHolidays() {

        logger.info("Fetching all holidays for employees");

        var holidays = holidayRepository.findAll();

        logger.debug("Total holidays fetched: {}", holidays.size());

        return new ApiResponse(
                200,
                "Holiday list fetched successfully",
                holidays
        );
    }
}