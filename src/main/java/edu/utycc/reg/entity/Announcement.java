package edu.utycc.reg.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private String pdfFileName; 

    private LocalDateTime publishDate;

    private LocalDateTime expiryDate;

    private boolean active = true;

    public Long getId() {
        return id;
    }

    public Announcement setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Announcement setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Announcement setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public Announcement setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
        return this;
    }

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public Announcement setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public Announcement setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Announcement setActive(boolean active) {
        this.active = active;
        return this;
    }


}
