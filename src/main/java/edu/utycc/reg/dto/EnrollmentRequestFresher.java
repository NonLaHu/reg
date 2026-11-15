package edu.utycc.reg.dto;

import java.util.List;

public class EnrollmentRequestFresher {
    private EnrollmentApplicationDto dto;
    private List<FileEntry> files;

    public static class FileEntry {
        private String fileTypeId;
        private String image; // base64 string
        // getters/setters
        public String getFileTypeId() {
            return fileTypeId;
        }
        public FileEntry setFileTypeId(String fileTypeId) {
            this.fileTypeId = fileTypeId;
            return this;
        }
        public String getImage() {
            return image;
        }
        public FileEntry setImage(String image) {
            this.image = image;
            return this;
        }
    }

    public EnrollmentApplicationDto getDto() {
        return dto;
    }

    public EnrollmentRequestFresher setDto(EnrollmentApplicationDto dto) {
        this.dto = dto;
        return this;
    }

    public List<FileEntry> getFiles() {
        return files;
    }

    public EnrollmentRequestFresher setFiles(List<FileEntry> files) {
        this.files = files;
        return this;
    }
}

