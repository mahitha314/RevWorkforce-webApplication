package com.revworkforce.employeeserviceImpl;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.employeeservice.HolidayService;
import com.revworkforce.repository.HolidayRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class EmployeeHolidayServiceImpl implements HolidayService {

	private static final Logger logger = LogManager.getLogger(EmployeeHolidayServiceImpl.class);

	private final HolidayRepository holidayRepository;

	public EmployeeHolidayServiceImpl(HolidayRepository holidayRepository) {
		this.holidayRepository = holidayRepository;
	}

	@Override
	public ApiResponse getAllHolidays() {

		logger.info("Employee requested holiday list");

		ApiResponse response = new ApiResponse(200, "Holiday list fetched successfully", holidayRepository.findAll());

		logger.debug("Holiday list returned successfully");

		return response;
	}

}