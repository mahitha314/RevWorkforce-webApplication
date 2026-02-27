package com.revworkforce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revworkforce.model.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    
}