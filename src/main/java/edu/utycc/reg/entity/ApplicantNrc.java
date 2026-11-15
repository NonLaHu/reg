package edu.utycc.reg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "applicant_nrc")
public class ApplicantNrc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false)
    private EnrollmentApplication application;

    @Enumerated(EnumType.STRING)
    @Column(name = "nrc_status")
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

    public ApplicantNrc setId(Long id) {
        this.id = id;
        return this;
    }

    public EnrollmentApplication getApplication() {
        return application;
    }

    public ApplicantNrc setApplication(EnrollmentApplication application) {
        this.application = application;
        return this;
    }

    public NrcStatus getNrcStatus() {
        return nrcStatus;
    }

    public ApplicantNrc setNrcStatus(NrcStatus nrcStatus) {
        this.nrcStatus = nrcStatus;
        return this;
    }

    public String getNrc() {
        return nrc;
    }

    public ApplicantNrc setNrc(String nrc) {
        this.nrc = nrc;
        return this;
    }

    public String getNationalNrc() {
        return nationalNrc;
    }

    public ApplicantNrc setNationalNrc(String nationalNrc) {
        this.nationalNrc = nationalNrc;
        return this;
    }

    
}
