package edu.utycc.reg.dto;

import java.time.LocalDateTime;

import edu.utycc.reg.entity.PreEnrollmentOldStudent;

public class PreEnrollmentOldDTO {

    private PreEnrollmentOldStudent.Gender gender;
    private Long id;
    private String fullNameMm;
    private String prevRollNo;
    private String email;
    private String nrc;
    private String phoneNumber;
    private String viberPhoneNumber;
    private String remarks;
    private String major;     
    private String majorYear;
    private PreEnrollmentOldStudent.Status status;
    private Boolean informed;
    private LocalDateTime createdAt;
    private String studentIdCardUrl;
    private String remark;
    
    
    public PreEnrollmentOldStudent.Status getStatus() {
        return status;
    }
    public PreEnrollmentOldDTO setStatus(PreEnrollmentOldStudent.Status status) {
        this.status = status;
        return this;
    }
    public PreEnrollmentOldStudent.Gender getGender() {
        return gender;
    }
    public PreEnrollmentOldDTO setGender(PreEnrollmentOldStudent.Gender gender) {
        this.gender = gender;
        return this;
    }
    public String getFullNameMm() {
        return fullNameMm;
    }
    public PreEnrollmentOldDTO setFullNameMm(String fullNameMm) {
        this.fullNameMm = fullNameMm;
        return this;
    }
    public String getPrevRollNo() {
        return prevRollNo;
    }
    public PreEnrollmentOldDTO setPrevRollNo(String prevRollNo) {
        this.prevRollNo = prevRollNo;
        return this;
    }
    public String getEmail() {
        return email;
    }
    public PreEnrollmentOldDTO setEmail(String email) {
        this.email = email;
        return this;
    }
    public String getNrc() {
        return nrc;
    }
    public PreEnrollmentOldDTO setNrc(String nrc) {
        this.nrc = nrc;
        return this;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public PreEnrollmentOldDTO setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
    public String getViberPhoneNumber() {
        return viberPhoneNumber;
    }
    public PreEnrollmentOldDTO setViberPhoneNumber(String viberPhoneNumber) {
        this.viberPhoneNumber = viberPhoneNumber;
        return this;
    }
    public String getRemarks() {
        return remarks;
    }
    public PreEnrollmentOldDTO setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }
    public String getMajor() {
        return major;
    }
    public PreEnrollmentOldDTO setMajor(String major) {
        this.major = major;
        return this;
    }
    public String getMajorYear() {
        return majorYear;
    }
    public PreEnrollmentOldDTO setMajorYear(String majorYear) {
        this.majorYear = majorYear;
        return this;
    }
    public Long getId() {
        return id;
    }
    public PreEnrollmentOldDTO setId(Long id) {
        this.id = id;
        return this;
    }
    public Boolean getInformed() {
        return informed;
    }
    public PreEnrollmentOldDTO setInformed(Boolean informed) {
        this.informed = informed;
        return this;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public PreEnrollmentOldDTO setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    public String getStudentIdCardUrl() {
        return studentIdCardUrl;
    }
    public PreEnrollmentOldDTO setStudentIdCardUrl(String studentIdCardUrl) {
        this.studentIdCardUrl = studentIdCardUrl;
        return this;
    }
    public String getRemark() {
        return remark;
    }
    public PreEnrollmentOldDTO setRemark(String remark) {
        this.remark = remark;
        return this;
    } 
    
}
