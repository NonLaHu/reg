package edu.utycc.reg.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "pre_enrollment_old_student")
public class PreEnrollmentOldStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_pin",unique = true, length = 10)
    private String trackingPin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(name = "full_name_mm", nullable = false, length = 100)
    private String fullNameMm;

    @Column(name = "prev_roll_no", nullable = false, length = 50)
    private String prevRollNo;

    @Column(name = "email", nullable = true, length = 255)
    private String email;

    @Column(nullable = true, unique = false, length = 50)
    private String nrc;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "viber_phone_number", length = 20)
    private String viberPhoneNumber;

    @Column(name = "student_id_card_url", length = 255)
    private String studentIdCardUrl; 

    @ManyToOne
    @JoinColumn(name = "major_id", nullable = false)
    private Major major;

    @ManyToOne
    @JoinColumn(name = "major_year_id", nullable = false)
    private MajorYear majorYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(nullable = false)
    private Boolean informed = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "remarks",columnDefinition = "TEXT")
    private String remarks;
    
    public enum Gender {
        MALE, FEMALE
    }

    public enum Status {
        PENDING, ACCEPTED, REJECTED
    }
    public Long getId() {
        return id;
    }

    public PreEnrollmentOldStudent setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFullNameMm() {
        return fullNameMm;
    }

    public PreEnrollmentOldStudent setFullNameMm(String fullNameMm) {
        this.fullNameMm = fullNameMm;
        return this;
    }

    public String getPrevRollNo() {
        return prevRollNo;
    }

    public PreEnrollmentOldStudent setPrevRollNo(String prevRollNo) {
        this.prevRollNo = prevRollNo;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public PreEnrollmentOldStudent setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public PreEnrollmentOldStudent setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getViberPhoneNumber() {
        return viberPhoneNumber;
    }

    public PreEnrollmentOldStudent setViberPhoneNumber(String viberPhoneNumber) {
        this.viberPhoneNumber = viberPhoneNumber;
        return this;
    }

    public Major getMajor() {
        return major;
    }

    public PreEnrollmentOldStudent setMajor(Major major) {
        this.major = major;
        return this;
    }

    public MajorYear getMajorYear() {
        return majorYear;
    }

    public PreEnrollmentOldStudent setMajorYear(MajorYear majorYear) {
        this.majorYear = majorYear;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public PreEnrollmentOldStudent setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Boolean getInformed() {
        return informed;
    }

    public PreEnrollmentOldStudent setInformed(Boolean informed) {
        this.informed = informed;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public PreEnrollmentOldStudent setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getRemarks() {
        return remarks;
    }

    public PreEnrollmentOldStudent setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public PreEnrollmentOldStudent setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public String getNrc() {
        return nrc;
    }

    public PreEnrollmentOldStudent setNrc(String nrc) {
        this.nrc = nrc;
        return this;
    }

    public String getStudentIdCardUrl() {
        return studentIdCardUrl;
    }

    public PreEnrollmentOldStudent setStudentIdCardUrl(String studentIdCardUrl) {
        this.studentIdCardUrl = studentIdCardUrl;
        return this;
    }

    
}
