package com.revworkforce.adminservice;

<<<<<<< HEAD
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revworkforce.model.Announcement;
import com.revworkforce.repository.AnnouncementRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    public Announcement addAnnouncement(Announcement announcement) {

        announcement.setPostedDate(LocalDate.now());

        return announcementRepository.save(announcement);
    }

    public Announcement getAnnouncementById(Long id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));
    }

    public Announcement updateAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }
}
=======
public class AnnouncementService {

}
>>>>>>> dev
