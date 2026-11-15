package edu.utycc.reg.dto;

import java.time.LocalDate;

public class ApplicantParentDto {
    private String parentType; // MOTHER | FATHER
    private String fullNameMm;
    private String fullNameEn;
    private String ethnic;
    private String religion;
    private String occupation;
    private LocalDate dateOfBirth;
    private Boolean isDeceased;
    private String dateOfDeath;
    private ApplicantParentNrcDto nrcs;
    private ApplicantParentContactDto contacts;
    private Long id;

    public Long getId() {
        return id;
    }
    public ApplicantParentDto setId(Long id) {
        this.id = id;
        return this;
    }
    public String getParentType() {
        return parentType;
    }
    public ApplicantParentDto setParentType(String parentType) {
        this.parentType = parentType;
        return this;
    }
    public String getFullNameMm() {
        return fullNameMm;
    }
    public ApplicantParentDto setFullNameMm(String fullNameMm) {
        this.fullNameMm = fullNameMm;
        return this;
    }
    public String getFullNameEn() {
        return fullNameEn;
    }
    public ApplicantParentDto setFullNameEn(String fullNameEn) {
        this.fullNameEn = fullNameEn;
        return this;
    }
    public String getEthnic() {
        return ethnic;
    }
    public ApplicantParentDto setEthnic(String ethnic) {
        this.ethnic = ethnic;
        return this;
    }
    public String getReligion() {
        return religion;
    }
    public ApplicantParentDto setReligion(String religion) {
        this.religion = religion;
        return this;
    }
    public String getOccupation() {
        return occupation;
    }
    public ApplicantParentDto setOccupation(String occupation) {
        this.occupation = occupation;
        return this;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public ApplicantParentDto setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }
    public Boolean getIsDeceased() {
        return isDeceased;
    }
    public ApplicantParentDto setIsDeceased(Boolean isDeceased) {
        this.isDeceased = isDeceased;
        return this;
    }
    public String getDateOfDeath() {
        return dateOfDeath;
    }
    public ApplicantParentDto setDateOfDeath(String dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
        return this;
    }
    public ApplicantParentNrcDto getNrcs() {
        return nrcs;
    }
    public ApplicantParentDto setNrcs(ApplicantParentNrcDto nrcs) {
        this.nrcs = nrcs;
        return this;
    }
    public ApplicantParentContactDto getContacts() {
        return contacts;
    }
    public ApplicantParentDto setContacts(ApplicantParentContactDto contacts) {
        this.contacts = contacts;
        return this;
    }

    
    
    
}
