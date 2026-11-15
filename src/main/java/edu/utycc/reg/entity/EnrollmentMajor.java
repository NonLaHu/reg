package edu.utycc.reg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "enrollment_majors",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"application_id", "major_id"})})
public class EnrollmentMajor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private EnrollmentApplication application;

    @ManyToOne
    @JoinColumn(name = "major_id", nullable = false)
    private Major major;

    @Column(name = "preference_rank", nullable = false)
    private Integer preferenceRank;

    public Long getId() {
        return id;
    }

    public EnrollmentMajor setId(Long id) {
        this.id = id;
        return this;
    }

    public EnrollmentApplication getApplication() {
        return application;
    }

    public EnrollmentMajor setApplication(EnrollmentApplication application) {
        this.application = application;
        return this;
    }

    public Major getMajor() {
        return major;
    }

    public EnrollmentMajor setMajor(Major major) {
        this.major = major;
        return this;
    }

    public Integer getPreferenceRank() {
        return preferenceRank;
    }

    public EnrollmentMajor setPreferenceRank(Integer preferenceRank) {
        this.preferenceRank = preferenceRank;
        return this;
    }

    
}
