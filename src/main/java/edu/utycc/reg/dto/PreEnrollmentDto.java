package edu.utycc.reg.dto;

import java.time.LocalDateTime;

public class PreEnrollmentDto {

    private Long id;
    private String gender;
    private String email;
    private String fullNameMm;
    private String uniEntranceRollno;
    private String uniEntranceAcademicYear;
    private String nrc;
    private String phoneNumber;
    private String viberPhoneNumber;
    private String status;
    private Boolean informed;
    private LocalDateTime createdAt;
    private String remark;
    private String rollEntranceOrder;
    private String fatherName;
    private String birthOfDate;
    private String trackingPin;

    private String passCertificateUrl; // URL to preview the file

    public Long getId() {
        return id;
    }

    public PreEnrollmentDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public PreEnrollmentDto setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public PreEnrollmentDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFullNameMm() {
        return fullNameMm;
    }

    public PreEnrollmentDto setFullNameMm(String fullNameMm) {
        this.fullNameMm = fullNameMm;
        return this;
    }

    public String getUniEntranceRollno() {
        return uniEntranceRollno;
    }

    public PreEnrollmentDto setUniEntranceRollno(String uniEntranceRollno) {
        this.uniEntranceRollno = uniEntranceRollno;
        return this;
    }

    public String getUniEntranceAcademicYear() {
        return uniEntranceAcademicYear;
    }

    public PreEnrollmentDto setUniEntranceAcademicYear(String uniEntranceAcademicYear) {
        this.uniEntranceAcademicYear = uniEntranceAcademicYear;
        return this;
    }

    public String getNrc() {
        return nrc;
    }

    public PreEnrollmentDto setNrc(String nrc) {
        this.nrc = nrc;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public PreEnrollmentDto setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getViberPhoneNumber() {
        return viberPhoneNumber;
    }

    public PreEnrollmentDto setViberPhoneNumber(String viberPhoneNumber) {
        this.viberPhoneNumber = viberPhoneNumber;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public PreEnrollmentDto setStatus(String status) {
        this.status = status;
        return this;
    }

    public Boolean getInformed() {
        return informed;
    }

    public PreEnrollmentDto setInformed(Boolean informed) {
        this.informed = informed;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public PreEnrollmentDto setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getPassCertificateUrl() {
        return passCertificateUrl;
    }

    public PreEnrollmentDto setPassCertificateUrl(String passCertificateUrl) {
        this.passCertificateUrl = passCertificateUrl;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public PreEnrollmentDto setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getRollEntranceOrder() {
        return rollEntranceOrder;
    }

    public PreEnrollmentDto setRollEntranceOrder(String rollEntranceOrder) {
        this.rollEntranceOrder = rollEntranceOrder;
        return this;
    }

    public String getFatherName() {
        return fatherName;
    }

    public PreEnrollmentDto setFatherName(String fatherName) {
        this.fatherName = fatherName;
        return this;
    }

    public String getBirthOfDate() {
        return birthOfDate;
    }

    public PreEnrollmentDto setBirthOfDate(String birthOfDate) {
        this.birthOfDate = birthOfDate;
        return this;
    }

    public String getTrackingPin() {
        return trackingPin;
    }

    public PreEnrollmentDto setTrackingPin(String trackingPin) {
        this.trackingPin = trackingPin;
        return this;
    }

    
}
