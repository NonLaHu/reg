package edu.utycc.reg.dto;

import java.time.LocalDateTime;

public class EnrollmentFileDto {
    private Integer fileTypeId;      // tells what kind of file (NRC, photo, etc.)
    private String fileData;         // Base64 string or data URL ("data:image/png;base64,AAAA...")
    private String fileName;         // optional: original filename
    private String contentType;      // optional: "image/png", "application/pdf"

    private String filePath;         // where you store after processing
    private LocalDateTime uploadedAt;
    private Long id;

    public Long getId() {
        return id;
    }
    public EnrollmentFileDto setId(Long id) {
        this.id = id;
        return this;
    }
    public Integer getFileTypeId() {
        return fileTypeId;
    }
    public EnrollmentFileDto setFileTypeId(Integer fileTypeId) {
        this.fileTypeId = fileTypeId;
        return this;
    }
    public String getFileData() {
        return fileData;
    }
    public EnrollmentFileDto setFileData(String fileData) {
        this.fileData = fileData;
        return this;
    }
    public String getFileName() {
        return fileName;
    }
    public EnrollmentFileDto setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
    public String getContentType() {
        return contentType;
    }
    public EnrollmentFileDto setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
    public String getFilePath() {
        return filePath;
    }
    public EnrollmentFileDto setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }
    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
    public EnrollmentFileDto setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
        return this;
    }
    
    
}
