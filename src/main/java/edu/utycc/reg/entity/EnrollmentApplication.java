package edu.utycc.reg.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollment_applications")
public class EnrollmentApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "is_fresher")
    private Boolean isFresher;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(name = "remark",columnDefinition = "TEXT")
    private String remark;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student studentId;


    public enum Status {
        PENDING,
        REJECTED,
        ACCEPTED
    }

    public Long getId() {
        return id;
    }

    public EnrollmentApplication setId(Long id) {
        this.id = id;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public EnrollmentApplication setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Boolean getIsFresher() {
        return isFresher;
    }

    public EnrollmentApplication setIsFresher(Boolean isFresher) {
        this.isFresher = isFresher;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public EnrollmentApplication setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public EnrollmentApplication setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Student getStudentId() {
        return studentId;
    }

    public EnrollmentApplication setStudentId(Student studentId) {
        this.studentId = studentId;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    
}
