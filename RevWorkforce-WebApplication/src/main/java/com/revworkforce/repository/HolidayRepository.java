package com.revworkforce.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.revworkforce.model.Holiday;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

	boolean existsByHolidayDate(LocalDate holidayDate);

	List<Holiday> findAllByOrderByHolidayDateAsc();

	List<Holiday> findByHolidayDateGreaterThanEqualOrderByHolidayDateAsc(LocalDate date);

}