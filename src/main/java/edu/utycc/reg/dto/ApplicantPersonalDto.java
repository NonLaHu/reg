package edu.utycc.reg.dto;

import java.time.LocalDate;

public class ApplicantPersonalDto {
    private Long id;
    private String fullNameMm;
    private String fullNameEn;
    private String gender; // map to enum in service
    private LocalDate dateOfBirth;
    private String ethnic;
    private String religion;
    
    public Long getId() {
        return id;
    }
    public ApplicantPersonalDto setId(Long id) {
        this.id = id;
        return this;
    }
    public String getFullNameMm() {
        return fullNameMm;
    }
    public ApplicantPersonalDto setFullNameMm(String fullNameMm) {
        this.fullNameMm = fullNameMm;
        return this;
    }
    public String getFullNameEn() {
        return fullNameEn;
    }
    public ApplicantPersonalDto setFullNameEn(String fullNameEn) {
        this.fullNameEn = fullNameEn;
        return this;
    }
    public String getGender() {
        return gender;
    }
    public ApplicantPersonalDto setGender(String gender) {
        this.gender = gender;
        return this;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public ApplicantPersonalDto setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }
    public String getEthnic() {
        return ethnic;
    }
    public ApplicantPersonalDto setEthnic(String ethnic) {
        this.ethnic = ethnic;
        return this;
    }
    public String getReligion() {
        return religion;
    }
    public ApplicantPersonalDto setReligion(String religion) {
        this.religion = religion;
        return this;
    }

    
}
