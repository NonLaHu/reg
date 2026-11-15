package edu.utycc.reg.dto;

import java.time.LocalDate;

import edu.utycc.reg.entity.EnrollmentApplication.Status;;


public class StudentDashboardDto {
    private Long id;
    private String name;
    private String rollNo;
    private String major;
    private String semester;
    private boolean enrolled;
    private LocalDate birthDate;
    private Status enrollmentStatus;
    private Long enrollmentId;
    // private String email;

    public Status getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public StudentDashboardDto setEnrollmentStatus(Status enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
        return this;
    }

    public String getName() {
        return name;
    }
    public StudentDashboardDto setName(String name) {
        this.name = name;
        return this;
    }
    public String getRollNo() {
        return rollNo;
    }
    public StudentDashboardDto setRollNo(String rollNo) {
        this.rollNo = rollNo;
        return this;
    }
    public String getMajor() {
        return major;
    }
    public StudentDashboardDto setMajor(String major) {
        this.major = major;
        return this;
    }
    public String getSemester() {
        return semester;
    }
    public StudentDashboardDto setSemester(String semester) {
        this.semester = semester;
        return this;
    }
    public boolean isEnrolled() {
        return enrolled;
    }
    public StudentDashboardDto setEnrolled(boolean enrolled) {
        this.enrolled = enrolled;
        return this;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public StudentDashboardDto setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }
    // public String getEmail() {
    //     return email;
    // }
    // public StudentDashboardDto setEmail(String email) {
    //     this.email = email;
    //     return this;
    // }

    public Long getId() {
        return id;
    }

    public StudentDashboardDto setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public StudentDashboardDto setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
        return this;
    }    
}
