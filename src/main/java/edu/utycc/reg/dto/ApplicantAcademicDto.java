package edu.utycc.reg.dto;

public class ApplicantAcademicDto {
    private Long id;
    private String academicYear;
    private String uniEntranceRollno;
    private String uniEntranceYear;
    private Short majorYearId;
    private String rollNo;

    public String getAcademicYear() {
        return academicYear;
    }
    public ApplicantAcademicDto setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
        return this;
    }
    public String getUniEntranceRollno() {
        return uniEntranceRollno;
    }
    public ApplicantAcademicDto setUniEntranceRollno(String uniEntranceRollno) {
        this.uniEntranceRollno = uniEntranceRollno;
        return this;
    }
    public String getUniEntranceYear() {
        return uniEntranceYear;
    }
    public ApplicantAcademicDto setUniEntranceYear(String uniEntranceYear) {
        this.uniEntranceYear = uniEntranceYear;
        return this;
    }
    public Short getMajorYearId() {
        return majorYearId;
    }
    public ApplicantAcademicDto setMajorYearId(Short majorYearId) {
        this.majorYearId = majorYearId;
        return this;
    }
    public String getRollNo() {
        return rollNo;
    }
    public ApplicantAcademicDto setRollNo(String rollNo) {
        this.rollNo = rollNo;
        return this;
    }
    public Long getId() {
        return id;
    }
    public ApplicantAcademicDto setId(Long id) {
        this.id = id;
        return this;
    }
    
    
}
