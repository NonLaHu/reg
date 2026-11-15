package edu.utycc.reg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "applicant_academic")
public class ApplicantAcademic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private EnrollmentApplication application;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "uni_entrance_rollno", nullable = false)
    private String uniEntranceRollno;

    @Column(name = "uni_entrance_year")
    private String uniEntranceYear;

    @ManyToOne
    @JoinColumn(name = "major_year_id")
    private MajorYear majorYear;

    @Column(name = "roll_no")
    private String rollNo;

    public Long getId() {
        return id;
    }

    public ApplicantAcademic setId(Long id) {
        this.id = id;
        return this;
    }

    public EnrollmentApplication getApplication() {
        return application;
    }

    public ApplicantAcademic setApplication(EnrollmentApplication application) {
        this.application = application;
        return this;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public ApplicantAcademic setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
        return this;
    }

    public String getUniEntranceRollno() {
        return uniEntranceRollno;
    }

    public ApplicantAcademic setUniEntranceRollno(String uniEntranceRollno) {
        this.uniEntranceRollno = uniEntranceRollno;
        return this;
    }

    public String getUniEntranceYear() {
        return uniEntranceYear;
    }

    public ApplicantAcademic setUniEntranceYear(String uniEntranceYear) {
        this.uniEntranceYear = uniEntranceYear;
        return this;
    }

    public MajorYear getMajorYear() {
        return majorYear;
    }

    public ApplicantAcademic setMajorYear(MajorYear majorYear) {
        this.majorYear = majorYear;
        return this;
    }

    public String getRollNo() {
        return rollNo;
    }

    public ApplicantAcademic setRollNo(String rollNo) {
        this.rollNo = rollNo;
        return this;
    }

    
}
