package edu.utycc.reg.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "applicant_parents")
public class ApplicantParent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private EnrollmentApplication application;

    @Enumerated(EnumType.STRING)
    @Column(name = "parent_type", nullable = false)
    private ParentType parentType;

    @Column(name = "full_name_mm", nullable = false, length = 100)
    private String fullNameMm;

    @Column(name = "full_name_en", nullable = false, length = 100)
    private String fullNameEn;

    @Column(length = 50)
    private String ethnic;

    @Column(length = 50)
    private String religion;

    @Column(length = 100)
    private String occupation;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "is_deceased", nullable = false)
    private Boolean isDeceased = false;

    @Column(name = "date_of_death", length = 255)
    private String dateOfDeath;

    @OneToMany(mappedBy = "applicantParent", cascade = CascadeType.ALL)
    private List<ApplicantParentNrc> nrcs;

    @OneToMany(mappedBy = "applicantParent", cascade = CascadeType.ALL)
    private List<ApplicantParentContact> contacts;

    public enum ParentType {
        MOTHER,
        FATHER,
        GUARDIAN
    }

    public Long getId() {
        return id;
    }

    public ApplicantParent setId(Long id) {
        this.id = id;
        return this;
    }

    public EnrollmentApplication getApplication() {
        return application;
    }

    public ApplicantParent setApplication(EnrollmentApplication application) {
        this.application = application;
        return this;
    }

    public ParentType getParentType() {
        return parentType;
    }

    public ApplicantParent setParentType(ParentType parentType) {
        this.parentType = parentType;
        return this;
    }

    public String getFullNameMm() {
        return fullNameMm;
    }

    public ApplicantParent setFullNameMm(String fullNameMm) {
        this.fullNameMm = fullNameMm;
        return this;
    }

    public String getFullNameEn() {
        return fullNameEn;
    }

    public ApplicantParent setFullNameEn(String fullNameEn) {
        this.fullNameEn = fullNameEn;
        return this;
    }

    public String getEthnic() {
        return ethnic;
    }

    public ApplicantParent setEthnic(String ethnic) {
        this.ethnic = ethnic;
        return this;
    }

    public String getReligion() {
        return religion;
    }

    public ApplicantParent setReligion(String religion) {
        this.religion = religion;
        return this;
    }

    public String getOccupation() {
        return occupation;
    }

    public ApplicantParent setOccupation(String occupation) {
        this.occupation = occupation;
        return this;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public ApplicantParent setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public Boolean getIsDeceased() {
        return isDeceased;
    }

    public ApplicantParent setIsDeceased(Boolean isDeceased) {
        this.isDeceased = isDeceased;
        return this;
    }


    public List<ApplicantParentNrc> getNrcs() {
        return nrcs;
    }

    public ApplicantParent setNrcs(List<ApplicantParentNrc> nrcs) {
        this.nrcs = nrcs;
        return this;
    }

    public List<ApplicantParentContact> getContacts() {
        return contacts;
    }

    public ApplicantParent setContacts(List<ApplicantParentContact> contacts) {
        this.contacts = contacts;
        return this;
    }

    public String getDateOfDeath() {
        return dateOfDeath;
    }

    public ApplicantParent setDateOfDeath(String dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
        return this;
    }

    
}
