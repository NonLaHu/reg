package edu.utycc.reg.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "major_year",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"major_id", "year_label"})})
public class MajorYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Short id;

    @ManyToOne
    @JoinColumn(name = "major_id", nullable = false)
    private Major major;

    @Column(name = "year_label", nullable = false, length = 50)
    private String yearLabel;

    @Column(name = "year_order", nullable = false)
    private Short yearOrder;

    @OneToMany(mappedBy = "majorYear")
    private List<ApplicantAcademic> applicantAcademics;

    public Short getId() {
        return id;
    }

    public MajorYear setId(Short id) {
        this.id = id;
        return this;
    }

    public Major getMajor() {
        return major;
    }

    public MajorYear setMajor(Major major) {
        this.major = major;
        return this;
    }

    public String getYearLabel() {
        return yearLabel;
    }

    public MajorYear setYearLabel(String yearLabel) {
        this.yearLabel = yearLabel;
        return this;
    }

    public Short getYearOrder() {
        return yearOrder;
    }

    public MajorYear setYearOrder(Short yearOrder) {
        this.yearOrder = yearOrder;
        return this;
    }

    public List<ApplicantAcademic> getApplicantAcademics() {
        return applicantAcademics;
    }

    public MajorYear setApplicantAcademics(List<ApplicantAcademic> applicantAcademics) {
        this.applicantAcademics = applicantAcademics;
        return this;
    }

    
}
