package com.revworkforce.adminservice;

import java.util.List;
import com.revworkforce.model.Announcement;

public interface AnnouncementService {

    Announcement saveAnnouncement(Announcement announcement);

    Announcement updateAnnouncement(Long id, Announcement announcement);

    void deleteAnnouncement(Long id);

    Announcement getAnnouncementById(Long id);

    List<Announcement> getAllAnnouncements();
}