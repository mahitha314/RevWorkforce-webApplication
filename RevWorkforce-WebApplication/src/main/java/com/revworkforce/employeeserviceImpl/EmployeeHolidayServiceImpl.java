package com.revworkforce.employeeserviceImpl;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.employeeservice.HolidayService;
import com.revworkforce.repository.HolidayRepository;

import org.springframework.stereotype.Service;

@Service
public class EmployeeHolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;

    public EmployeeHolidayServiceImpl(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    @Override
    public ApiResponse getAllHolidays() {

        return new ApiResponse(
                200,
                "Holiday list fetched successfully",
                holidayRepository.findAll()
        );
    }
}