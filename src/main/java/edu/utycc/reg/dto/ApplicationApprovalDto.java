package edu.utycc.reg.dto;

import java.time.LocalDateTime;

public class ApplicationApprovalDto {
    private Long id;
    private String approverRole;   // stored as enum name in entity
    private String status;         // stored as enum name in entity
    private LocalDateTime approvedAt;
    private String remarks;

    public ApplicationApprovalDto() {
    }

    public ApplicationApprovalDto(Long id, String approverRole, String status, LocalDateTime approvedAt, String remarks) {
        this.id = id;
        this.approverRole = approverRole;
        this.status = status;
        this.approvedAt = approvedAt;
        this.remarks = remarks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApproverRole() {
        return approverRole;
    }

    public void setApproverRole(String approverRole) {
        this.approverRole = approverRole;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
