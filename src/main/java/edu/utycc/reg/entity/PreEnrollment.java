package edu.utycc.reg.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "pre_enrollment")
public class PreEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // New fields
    @Column(name = "father_name", length = 100)
    private String fatherName;

    @Column(name = "birth_of_date") // keeps your "birthOfDate" naming in the entity
    private LocalDate birthOfDate;

    @Column(name = "roll_entrance_order", length = 20)
    private String rollEntranceOrder;

    @Column(name = "tracking_pin", unique = true, length = 10)
    private String trackingPin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = true, length = 255)
    private String email;

    @Column(name = "full_name_mm", nullable = false, length = 100)
    private String fullNameMm;

    @Column(name = "uni_entrance_rollno", nullable = false, length = 50)
    private String uniEntranceRollno;

    @Column(name = "uni_entrance_academic_year", nullable = false, length = 20)
    private String uniEntranceAcademicYear;

    @Column(unique = false, length = 50, nullable = true)
    private String nrc;

    @Column(name = "pass_certificate", length = 255)
    private String passCertificate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @Column(nullable = false)
    private Boolean informed = false;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "viber_phone_number", length = 20)
    private String viberPhoneNumber;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "remarks", columnDefinition = "TEXT")
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

    public PreEnrollment setId(Long id) {
        this.id = id;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public PreEnrollment setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public PreEnrollment setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFullNameMm() {
        return fullNameMm;
    }

    public PreEnrollment setFullNameMm(String fullNameMm) {
        this.fullNameMm = fullNameMm;
        return this;
    }

    public String getUniEntranceRollno() {
        return uniEntranceRollno;
    }

    public PreEnrollment setUniEntranceRollno(String uniEntranceRollno) {
        this.uniEntranceRollno = uniEntranceRollno;
        return this;
    }

    public String getUniEntranceAcademicYear() {
        return uniEntranceAcademicYear;
    }

    public PreEnrollment setUniEntranceAcademicYear(String uniEntranceAcademicYear) {
        this.uniEntranceAcademicYear = uniEntranceAcademicYear;
        return this;
    }

    public String getNrc() {
        return nrc;
    }

    public PreEnrollment setNrc(String nrc) {
        this.nrc = nrc;
        return this;
    }

    public String getPassCertificate() {
        return passCertificate;
    }

    public PreEnrollment setPassCertificate(String passCertificate) {
        this.passCertificate = passCertificate;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public PreEnrollment setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Boolean getInformed() {
        return informed;
    }

    public PreEnrollment setInformed(Boolean informed) {
        this.informed = informed;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public PreEnrollment setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getViberPhoneNumber() {
        return viberPhoneNumber;
    }

    public PreEnrollment setViberPhoneNumber(String viberPhoneNumber) {
        this.viberPhoneNumber = viberPhoneNumber;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public PreEnrollment setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getRemarks() {
        return remarks;
    }

    public PreEnrollment setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public String getTrackingPin() {
        return trackingPin;
    }

    public PreEnrollment setTrackingPin(String trackingPin) {
        this.trackingPin = trackingPin;
        return this;
    }

    public String getFatherName() {
        return fatherName;
    }

    public PreEnrollment setFatherName(String fatherName) {
        this.fatherName = fatherName;
        return this;
    }

    public LocalDate getBirthOfDate() {
        return birthOfDate;
    }

    public PreEnrollment setBirthOfDate(LocalDate birthOfDate) {
        this.birthOfDate = birthOfDate;
        return this;
    }

    public String getRollEntranceOrder() {
        return rollEntranceOrder;
    }

    public PreEnrollment setRollEntranceOrder(String rollEntranceOrder) {
        this.rollEntranceOrder = rollEntranceOrder;
        return this;
    }

}
