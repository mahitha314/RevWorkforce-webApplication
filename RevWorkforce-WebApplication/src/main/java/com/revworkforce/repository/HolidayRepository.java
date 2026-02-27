package com.revworkforce.repository;

import com.revworkforce.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    List<Holiday> findByHolidayDate(LocalDate holidayDate);

    List<Holiday> findByHolidayDateBetween(LocalDate startDate,
                                           LocalDate endDate);
}