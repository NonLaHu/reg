package edu.utycc.reg.dto;

import edu.utycc.reg.entity.EnrollmentDorm.Status;

public class EnrollmentDormDto {
    private String dormFeeResponsible; // mother | father | other
    private String otherName;
    private String otherRelationship;
    private String otherAddress;
    private Boolean scholarshipSupport;
    private String scholarshipType;
    private String anotherScholarship;
    private Long id;
    private Status status;

    public Long getId() {
        return id;
    }
    public EnrollmentDormDto setId(Long id) {
        this.id = id;
        return this;
    }
    public String getDormFeeResponsible() {
        return dormFeeResponsible;
    }
    public EnrollmentDormDto setDormFeeResponsible(String dormFeeResponsible) {
        this.dormFeeResponsible = dormFeeResponsible;
        return this;
    }
    public String getOtherName() {
        return otherName;
    }
    public EnrollmentDormDto setOtherName(String otherName) {
        this.otherName = otherName;
        return this;
    }
    public String getOtherRelationship() {
        return otherRelationship;
    }
    public EnrollmentDormDto setOtherRelationship(String otherRelationship) {
        this.otherRelationship = otherRelationship;
        return this;
    }
    public String getOtherAddress() {
        return otherAddress;
    }
    public EnrollmentDormDto setOtherAddress(String otherAddress) {
        this.otherAddress = otherAddress;
        return this;
    }
    public Boolean getScholarshipSupport() {
        return scholarshipSupport;
    }
    public EnrollmentDormDto setScholarshipSupport(Boolean scholarshipSupport) {
        this.scholarshipSupport = scholarshipSupport;
        return this;
    }
    public String getScholarshipType() {
        return scholarshipType;
    }
    public EnrollmentDormDto setScholarshipType(String scholarshipType) {
        this.scholarshipType = scholarshipType;
        return this;
    }
    public String getAnotherScholarship() {
        return anotherScholarship;
    }
    public EnrollmentDormDto setAnotherScholarship(String anotherScholarship) {
        this.anotherScholarship = anotherScholarship;
        return this;
    }
    public Status getStatus() {
        return status;
    }
    public EnrollmentDormDto setStatus(Status status) {
        this.status = status;
        return this;
    }
    
    
}
