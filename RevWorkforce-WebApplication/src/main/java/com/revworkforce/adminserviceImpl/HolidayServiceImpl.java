package com.revworkforce.adminserviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revworkforce.adminservice.HolidayService;
import com.revworkforce.model.Holiday;
import com.revworkforce.repository.HolidayRepository;

@Service
@Transactional
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayServiceImpl(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    @Override
    public Holiday saveHoliday(Holiday holiday) {

        if (holidayRepository.existsByHolidayDate(holiday.getHolidayDate())) {
            throw new RuntimeException("Holiday already exists for this date.");
        }

        return holidayRepository.save(holiday);
    }

    @Override
    public Holiday updateHoliday(Long id, Holiday holiday) {

        Holiday existing = holidayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday not found"));

        existing.setHolidayName(holiday.getHolidayName());
        existing.setHolidayDate(holiday.getHolidayDate());
        existing.setDescription(holiday.getDescription());

        return holidayRepository.save(existing);
    }

    @Override
    public void deleteHoliday(Long id) {
        holidayRepository.deleteById(id);
    }

    @Override
    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAllByOrderByHolidayDateAsc();
    }

    @Override
    public Holiday getHolidayById(Long id) {
        return holidayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Holiday not found"));
    }
}