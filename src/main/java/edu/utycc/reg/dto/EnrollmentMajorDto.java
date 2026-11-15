package edu.utycc.reg.dto;

public class EnrollmentMajorDto {
    private Short majorId;
    private Integer preferenceRank;
    private Long id;

    public Long getId() {
        return id;
    }
    public EnrollmentMajorDto setId(Long id) {
        this.id = id;
        return this;
    }
    public Short getMajorId() {
        return majorId;
    }
    public EnrollmentMajorDto setMajorId(Short majorId) {
        this.majorId = majorId;
        return this;
    }
    public Integer getPreferenceRank() {
        return preferenceRank;
    }
    public EnrollmentMajorDto setPreferenceRank(Integer preferenceRank) {
        this.preferenceRank = preferenceRank;
        return this;
    }

    
}
