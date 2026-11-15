package edu.utycc.reg.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pre_enrollment_id")
    private PreEnrollment preEnrollment;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne
    @JoinColumn(name = "major_year_id")
    private MajorYear semester;

    @Column(nullable = false)
    private boolean enrolled = false;

    @Column(nullable = false, length = 100 )
    private String name;

    @Column(name = "roll_no", nullable = false, length = 10)
    private String rollNo;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public Student setId(Long id) {
        this.id = id;
        return this;
    }

    public PreEnrollment getPreEnrollment() {
        return preEnrollment;
    }

    public Student setPreEnrollment(PreEnrollment preEnrollment) {
        this.preEnrollment = preEnrollment;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Student setUser(User user) {
        this.user = user;
        return this;
    }

    public boolean isEnrolled() {
        return enrolled;
    }

    public Student setEnrolled(boolean enrolled) {
        this.enrolled = enrolled;
        return this;
    }

    public String getName() {
        return name;
    }

    public Student setName(String name) {
        this.name = name;
        return this;
    }

    public String getRollNo() {
        return rollNo;
    }

    public Student setRollNo(String rollNo) {
        this.rollNo = rollNo;
        return this;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Student setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Student setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Major getMajor() {
        return major;
    }

    public Student setMajor(Major major) {
        this.major = major;
        return this;
    }

    public MajorYear getSemester() {
        return semester;
    }

    public Student setSemester(MajorYear semester) {
        this.semester = semester;
        return this;
    }

    
}