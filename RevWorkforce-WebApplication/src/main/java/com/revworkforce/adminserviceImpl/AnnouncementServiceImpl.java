package com.revworkforce.adminserviceImpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revworkforce.adminservice.AnnouncementService;
import com.revworkforce.model.Announcement;
import com.revworkforce.repository.AnnouncementRepository;
//import com.revworkforce.service.AnnouncementService;

@Service
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository repository;

    public AnnouncementServiceImpl(AnnouncementRepository repository) {
        this.repository = repository;
    }

    @Override
    public Announcement saveAnnouncement(Announcement announcement) {
        announcement.setPostedDate(LocalDate.now());
        return repository.save(announcement);
    }

    @Override
    public Announcement updateAnnouncement(Long id, Announcement announcement) {
        Announcement existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));

        existing.setTitle(announcement.getTitle());
        existing.setMessage(announcement.getMessage());

        return repository.save(existing);
    }

    @Override
    public void deleteAnnouncement(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Announcement getAnnouncementById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));
    }

    @Override
    public List<Announcement> getAllAnnouncements() {
        return repository.findAllByOrderByPostedDateDesc();
    }
}