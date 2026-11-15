package edu.utycc.reg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student_id_link")
public class OldStudentTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "old_student_id", referencedColumnName = "id", unique = true)
    private Student oldStudent;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pre_enrollment_id", referencedColumnName = "id", unique = true)
    private PreEnrollmentOldStudent preEnrollment;

    public Long getId() {
        return id;
    }

    public OldStudentTrack setId(Long id) {
        this.id = id;
        return this;
    }

    public Student getOldStudent() {
        return oldStudent;
    }

    public OldStudentTrack setOldStudent(Student oldStudent) {
        this.oldStudent = oldStudent;
        return this;
    }

    public PreEnrollmentOldStudent getPreEnrollment() {
        return preEnrollment;
    }

    public OldStudentTrack setPreEnrollment(PreEnrollmentOldStudent preEnrollment) {
        this.preEnrollment = preEnrollment;
        return this;
    }


}