package edu.utycc.reg.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "application_approvals")
public class ApplicationApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private EnrollmentApplication application;

    @Enumerated(EnumType.STRING)
    @Column(name = "approver_role", nullable = false)
    private ApproverRole approverRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApprovalStatus status = ApprovalStatus.pending;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    public enum ApproverRole {
        student_affair, hod, dorm_warden, finance, admin
    }

    public enum ApprovalStatus {
        pending, rejected, approved
    }

    public Long getId() {
        return id;
    }

    public ApplicationApproval setId(Long id) {
        this.id = id;
        return this;
    }

    public EnrollmentApplication getApplication() {
        return application;
    }

    public ApplicationApproval setApplication(EnrollmentApplication application) {
        this.application = application;
        return this;
    }

    public ApproverRole getApproverRole() {
        return approverRole;
    }

    public ApplicationApproval setApproverRole(ApproverRole approverRole) {
        this.approverRole = approverRole;
        return this;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public ApplicationApproval setStatus(ApprovalStatus status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public ApplicationApproval setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
        return this;
    }

    public String getRemarks() {
        return remarks;
    }

    public ApplicationApproval setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    
}
