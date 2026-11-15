package edu.utycc.reg.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "applicant_personal")
public class ApplicantPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false)
    private EnrollmentApplication application;

    @Column(name = "full_name_mm", nullable = false)
    private String fullNameMm;

    @Column(name = "full_name_en", nullable = false)
    private String fullNameEn;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "date_of_birth", nullable = true)
    private LocalDate dateOfBirth;

    @Column(name = "ethnic", length = 50)
    private String ethnic;

    @Column(name = "religion", length = 50)
    private String religion;

    public enum Gender {
        MALE,
        FEMALE
    }

    public Long getId() {
        return id;
    }

    public ApplicantPersonal setId(Long id) {
        this.id = id;
        return this;
    }

    public EnrollmentApplication getApplication() {
        return application;
    }

    public ApplicantPersonal setApplication(EnrollmentApplication application) {
        this.application = application;
        return this;
    }

    public String getFullNameMm() {
        return fullNameMm;
    }

    public ApplicantPersonal setFullNameMm(String fullNameMm) {
        this.fullNameMm = fullNameMm;
        return this;
    }

    public String getFullNameEn() {
        return fullNameEn;
    }

    public ApplicantPersonal setFullNameEn(String fullNameEn) {
        this.fullNameEn = fullNameEn;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public ApplicantPersonal setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public ApplicantPersonal setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public String getEthnic() {
        return ethnic;
    }

    public ApplicantPersonal setEthnic(String ethnic) {
        this.ethnic = ethnic;
        return this;
    }

    public String getReligion() {
        return religion;
    }

    public ApplicantPersonal setReligion(String religion) {
        this.religion = religion;
        return this;
    }

    
}
