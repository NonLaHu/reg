package edu.utycc.reg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "applicant_parents_nrc")
public class ApplicantParentNrc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "applicant_parent_id", nullable = false)
    private ApplicantParent applicantParent;

    @Enumerated(EnumType.STRING)
    @Column(name = "nrc_status", nullable = false)
    private NrcStatus nrcStatus = NrcStatus.AVAILABLE;

    @Column(name = "nrc" , length = 50)
    private String nrc;

    @Column(name = "national_nrc", length = 50)
    private String nationalNrc;

    public enum NrcStatus {
        PENDING,
        AVAILABLE
    }

    public Long getId() {
        return id;
    }

    public ApplicantParentNrc setId(Long id) {
        this.id = id;
        return this;
    }

    public ApplicantParent getApplicantParent() {
        return applicantParent;
    }

    public ApplicantParentNrc setApplicantParent(ApplicantParent applicantParent) {
        this.applicantParent = applicantParent;
        return this;
    }

    public NrcStatus getNrcStatus() {
        return nrcStatus;
    }

    public ApplicantParentNrc setNrcStatus(NrcStatus nrcStatus) {
        this.nrcStatus = nrcStatus;
        return this;
    }

    public String getNrc() {
        return nrc;
    }

    public ApplicantParentNrc setNrc(String nrc) {
        this.nrc = nrc;
        return this;
    }

    public String getNationalNrc() {
        return nationalNrc;
    }

    public ApplicantParentNrc setNationalNrc(String nationalNrc) {
        this.nationalNrc = nationalNrc;
        return this;
    }

    
}
