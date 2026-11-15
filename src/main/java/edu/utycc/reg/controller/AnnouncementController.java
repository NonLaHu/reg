package edu.utycc.reg.controller;

import edu.utycc.reg.entity.Announcement;
import edu.utycc.reg.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN')")
    public Announcement createAnnouncement(@RequestParam String title,
                                           @RequestParam(required = false) String description,
                                           @RequestParam MultipartFile pdf,
                                           @RequestParam(required = false) LocalDateTime publishDate,
                                           @RequestParam(required = false) LocalDateTime expiryDate) throws IOException {
        return announcementService.createAnnouncement(title, description, pdf, publishDate, expiryDate);
    }

    @GetMapping("/active")
    public List<Announcement> getActiveAnnouncements() {
        return announcementService.getActiveAnnouncements();
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<FileSystemResource> downloadFile(@PathVariable String fileName) {
        File file = new File("uploads/announcements/" + fileName);
        if (!file.exists()) return ResponseEntity.notFound().build();

        FileSystemResource resource = new FileSystemResource(file);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}
