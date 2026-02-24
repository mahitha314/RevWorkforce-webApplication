package com.workforce.repository;

import com.workforce.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository 
        extends JpaRepository<Announcement, Long> {

}