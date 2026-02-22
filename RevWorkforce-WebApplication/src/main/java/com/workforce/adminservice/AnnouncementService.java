package com.workforce.adminservice;

import com.workforce.model.Announcement;
import com.workforce.repository.AnnouncementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }
    public Announcement createAnnouncement(Announcement announcement) {

        if (announcement.getTitle() == null || announcement.getTitle().isBlank()) {
            throw new RuntimeException("Title cannot be empty");
        }

        if (announcement.getMessage() == null || announcement.getMessage().isBlank()) {
            throw new RuntimeException("Message cannot be empty");
        }

        announcement.setPostedDate(LocalDate.now());

        return announcementRepository.save(announcement);
    }
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }
    public Announcement getAnnouncementById(Long id) {
        return announcementRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Announcement not found with ID: " + id));
    }
    public Announcement updateAnnouncement(Long id, Announcement updatedAnnouncement) {

        Announcement existing = getAnnouncementById(id);

        if (updatedAnnouncement.getTitle() == null || updatedAnnouncement.getTitle().isBlank()) {
            throw new RuntimeException("Title cannot be empty");
        }

        if (updatedAnnouncement.getMessage() == null || updatedAnnouncement.getMessage().isBlank()) {
            throw new RuntimeException("Message cannot be empty");
        }

        existing.setTitle(updatedAnnouncement.getTitle());
        existing.setMessage(updatedAnnouncement.getMessage());

        return announcementRepository.save(existing);
    }
    public void deleteAnnouncement(Long id) {

        if (!announcementRepository.existsById(id)) {
            throw new RuntimeException("Announcement not found with ID: " + id);
        }

        announcementRepository.deleteById(id);
    }
}