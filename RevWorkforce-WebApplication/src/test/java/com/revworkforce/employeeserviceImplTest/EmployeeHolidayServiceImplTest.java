package com.revworkforce.employeeserviceImplTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.employeeserviceImpl.EmployeeHolidayServiceImpl;
import com.revworkforce.model.Holiday;
import com.revworkforce.repository.HolidayRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeeHolidayServiceImplTest {

	@InjectMocks
	private EmployeeHolidayServiceImpl service;

	@Mock
	private HolidayRepository holidayRepository;

	private Holiday holiday;

	@BeforeEach
	void setUp() {
		holiday = new Holiday();
		holiday.setId(1L);
		holiday.setHolidayName("Independence Day");
	}

	@Test
	public void getAllHolidays_Positive() {

		when(holidayRepository.findAll()).thenReturn(Arrays.asList(holiday));

		ApiResponse response = service.getAllHolidays();

		assertEquals(200, response.getStatus());
		assertEquals("Holiday list fetched successfully", response.getMessage());
		assertNotNull(response.getData());

		verify(holidayRepository).findAll();
	}

}