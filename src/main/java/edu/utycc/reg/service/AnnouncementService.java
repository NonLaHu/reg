package edu.utycc.reg.service;

import edu.utycc.reg.entity.Announcement;
import edu.utycc.reg.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class AnnouncementService {

    private final String uploadDir = "uploads/announcements/";

    @Autowired
    private AnnouncementRepository announcementRepository;

    public Announcement createAnnouncement(String title, String description,
            MultipartFile pdfFile,
            LocalDateTime publishDate,
            LocalDateTime expiryDate) throws IOException {
        Announcement ann = new Announcement();
        ann.setTitle(title);
        ann.setDescription(description);
        ann.setPublishDate(publishDate != null ? publishDate : LocalDateTime.now());
        ann.setExpiryDate(expiryDate);

        // Save file
        if (!pdfFile.isEmpty()) {
            File folder = new File(uploadDir);
            if (!folder.exists())
                folder.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + pdfFile.getOriginalFilename();
            Path path = Paths.get(uploadDir, fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, pdfFile.getBytes());

            ann.setPdfFileName(fileName);
        }

        return announcementRepository.save(ann);
    }

    public List<Announcement> getActiveRecentAnnouncements() {
        LocalDateTime now = LocalDateTime.now();
        List<Announcement> list = announcementRepository.findActivePublished(now, PageRequest.of(0, 2));
        return list == null ? Collections.emptyList() : list;
    }

    public List<Announcement> getActiveAnnouncements() {
        LocalDateTime now = LocalDateTime.now();
        return announcementRepository.findByActiveTrueAndPublishDateBeforeAndExpiryDateAfter(now, now);
    }

    public Page<Announcement> getActiveAnnouncementsPage(int page, int size) {
        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = PageRequest.of(page, size);
        return announcementRepository.findActiveAnnouncements(pageable, now);
    }

    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    public Announcement getById(Long id) {
        return announcementRepository.getById(id);
    }

    public Page<Announcement> getAnnoucements(String filter, Pageable pageable) {
        if (filter == null || filter.isBlank()) {
            return announcementRepository.findAll(pageable);
        } else {
            return announcementRepository.findByTitleContainingIgnoreCaseOrPdfFileNameContainingIgnoreCase(filter,
                    filter, pageable);
        }
    }

    public void deleteAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));

        if (announcement.getPdfFileName() != null) {
            File file = new File(uploadDir, announcement.getPdfFileName());
            if (file.exists()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    // you can log this instead of throwing to avoid partial deletion
                    System.err.println("Warning: failed to delete file " + file.getAbsolutePath());
                }
            }
        }

        announcementRepository.deleteById(id);
    }
}
