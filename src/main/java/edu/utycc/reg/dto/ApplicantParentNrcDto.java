package edu.utycc.reg.dto;

public class ApplicantParentNrcDto {
    private String nrcStatus; // PENDING | AVAILABLE
    private String nrc;
    private String nationalNrc;
    private Long id;

    public Long getId() {
        return id;
    }
    public ApplicantParentNrcDto setId(Long id) {
        this.id = id;
        return this;
    }
    public String getNrcStatus() {
        return nrcStatus;
    }
    public ApplicantParentNrcDto setNrcStatus(String nrcStatus) {
        this.nrcStatus = nrcStatus;
        return this;
    }
    public String getNrc() {
        return nrc;
    }
    public ApplicantParentNrcDto setNrc(String nrc) {
        this.nrc = nrc;
        return this;
    }
    public String getNationalNrc() {
        return nationalNrc;
    }
    public ApplicantParentNrcDto setNationalNrc(String nationalNrc) {
        this.nationalNrc = nationalNrc;
        return this;
    }

    
}
