package edu.utycc.reg.dto;

public class ApplicantNrcDto {
    private String nrcStatus; // PENDING | AVAILABLE
    private String nrc;
    private String nationalNrc;
    private Long id;

    public Long getId() {
        return id;
    }
    public ApplicantNrcDto setId(Long id) {
        this.id = id;
        return this;
    }
    public String getNrcStatus() {
        return nrcStatus;
    }
    public ApplicantNrcDto setNrcStatus(String nrcStatus) {
        this.nrcStatus = nrcStatus;
        return this;
    }
    public String getNrc() {
        return nrc;
    }
    public ApplicantNrcDto setNrc(String nrc) {
        this.nrc = nrc;
        return this;
    }
    public String getNationalNrc() {
        return nationalNrc;
    }
    public ApplicantNrcDto setNationalNrc(String nationalNrc) {
        this.nationalNrc = nationalNrc;
        return this;
    }
}
