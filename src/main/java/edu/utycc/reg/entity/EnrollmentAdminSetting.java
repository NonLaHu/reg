package edu.utycc.reg.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "enrollment_admin_setting")
public class EnrollmentAdminSetting {
    @Id
    private Long id = 1L; // fixed single row

    @Column(name = "enrollment_start")
    private LocalDate enrollmentStart;

    @Column(name = "enrollment_end")
    private LocalDate enrollmentEnd;

    @Column(name = "enrollment_open")
    private Boolean enrollmentOpen;

    @Column(name = "academic_year")
    private String academicYear;

    public Long getId() {
        return id;
    }

    public EnrollmentAdminSetting setId(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getEnrollmentStart() {
        return enrollmentStart;
    }

    public EnrollmentAdminSetting setEnrollmentStart(LocalDate enrollmentStart) {
        this.enrollmentStart = enrollmentStart;
        return this;
    }

    public LocalDate getEnrollmentEnd() {
        return enrollmentEnd;
    }

    public EnrollmentAdminSetting setEnrollmentEnd(LocalDate enrollmentEnd) {
        this.enrollmentEnd = enrollmentEnd;
        return this;
    }

    public Boolean getEnrollmentOpen() {
        return enrollmentOpen;
    }

    public EnrollmentAdminSetting setEnrollmentOpen(Boolean enrollmentOpen) {
        this.enrollmentOpen = enrollmentOpen;
        return this;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public EnrollmentAdminSetting setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
        return this;
    }

    
}
