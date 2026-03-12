package com.revworkforce.adminservice;

import java.util.List;
import com.revworkforce.model.Holiday;

public interface HolidayService {

    Holiday saveHoliday(Holiday holiday);

    Holiday updateHoliday(Long id, Holiday holiday);

    void deleteHoliday(Long id);

    List<Holiday> getAllHolidays();

    Holiday getHolidayById(Long id);
}