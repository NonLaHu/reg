package edu.utycc.reg.dto;

import edu.utycc.reg.entity.PreEnrollmentOldStudent.Gender;

public class RequestPreEnrollmentOldDTO {

    private Gender gender;
    private String fullNameMm;
    private String prevRollNo;
    private String email;
    private String nrc;
    private String phoneNumber;
    private String viberPhoneNumber;
    private Short majorId;
    private Short majorYearId;

    public Gender getGender() {
        return gender;
    }
    public RequestPreEnrollmentOldDTO setGender(Gender gender) {
        this.gender = gender;
        return this;
    }
    public String getFullNameMm() {
        return fullNameMm;
    }
    public RequestPreEnrollmentOldDTO setFullNameMm(String fullNameMm) {
        this.fullNameMm = fullNameMm;
        return this;
    }
    public String getPrevRollNo() {
        return prevRollNo;
    }
    public RequestPreEnrollmentOldDTO setPrevRollNo(String prevRollNo) {
        this.prevRollNo = prevRollNo;
        return this;
    }
    public String getEmail() {
        return email;
    }
    public RequestPreEnrollmentOldDTO setEmail(String email) {
        this.email = email;
        return this;
    }
    public String getNrc() {
        return nrc;
    }
    public RequestPreEnrollmentOldDTO setNrc(String nrc) {
        this.nrc = nrc;
        return this;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public RequestPreEnrollmentOldDTO setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
    public String getViberPhoneNumber() {
        return viberPhoneNumber;
    }
    public RequestPreEnrollmentOldDTO setViberPhoneNumber(String viberPhoneNumber) {
        this.viberPhoneNumber = viberPhoneNumber;
        return this;
    }
    public Short getMajorId() {
        return majorId;
    }
    public RequestPreEnrollmentOldDTO setMajorId(Short majorId) {
        this.majorId = majorId;
        return this;
    }
    public Short getMajorYearId() {
        return majorYearId;
    }
    public RequestPreEnrollmentOldDTO setMajorYearId(Short majorYearId) {
        this.majorYearId = majorYearId;
        return this;
    }

    
    // getters and setters
}
