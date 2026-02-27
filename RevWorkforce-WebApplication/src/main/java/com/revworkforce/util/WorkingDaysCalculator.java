package com.revworkforce.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class WorkingDaysCalculator {

    public static int calculateDays(LocalDate start,
                                    LocalDate end,
                                    List<LocalDate> holidays) {

        int days = 0;

        for (LocalDate date = start;
             !date.isAfter(end);
             date = date.plusDays(1)) {

            if (date.getDayOfWeek() != DayOfWeek.SATURDAY &&
                date.getDayOfWeek() != DayOfWeek.SUNDAY &&
                !holidays.contains(date)) {

                days++;
            }
        }

        return days;
    }
}