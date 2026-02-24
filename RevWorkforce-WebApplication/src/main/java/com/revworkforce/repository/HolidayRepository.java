package com.revworkforce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revworkforce.model.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
}