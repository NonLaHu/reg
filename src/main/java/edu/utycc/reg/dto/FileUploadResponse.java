package edu.utycc.reg.dto;


public class FileUploadResponse {
    private String filePath;

    public FileUploadResponse() {}

    public FileUploadResponse(String filePath) { this.filePath = filePath; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
}
