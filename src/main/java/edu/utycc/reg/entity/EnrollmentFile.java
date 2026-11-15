package edu.utycc.reg.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollment_files")
public class EnrollmentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private EnrollmentApplication application;

    @ManyToOne
    @JoinColumn(name = "file_type_id", nullable = false)
    private FileType fileType;

    @Column(name = "file_path", nullable = false, length = 255)
    private String filePath;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public EnrollmentFile setId(Long id) {
        this.id = id;
        return this;
    }

    public EnrollmentApplication getApplication() {
        return application;
    }

    public EnrollmentFile setApplication(EnrollmentApplication application) {
        this.application = application;
        return this;
    }

    public FileType getFileType() {
        return fileType;
    }

    public EnrollmentFile setFileType(FileType fileType) {
        this.fileType = fileType;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public EnrollmentFile setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public EnrollmentFile setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
        return this;
    }

   
}
