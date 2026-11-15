package edu.utycc.reg.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "enrollment_dorm")
public class EnrollmentDorm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private EnrollmentApplication application;

    @Enumerated(EnumType.STRING)
    @Column(name = "dorm_fee_responsible")
    private DormFeeResponsible dormFeeResponsible;

    @Column(name = "other_name", length = 100)
    private String otherName;

    @Column(name = "other_relationship", length = 50)
    private String otherRelationship;

    @Column(name = "other_address", columnDefinition = "TEXT")
    private String otherAddress;

    @Column(name = "scholarship_support")
    private Boolean scholarshipSupport = false;

    @Column(name = "scholarship_type", length = 100)
    private String scholarshipType;

    @Column(name = "another_scholarship", length = 100)
    private String anotherScholarship;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING,
        REJECTED,
        ACCEPTED
    }

    public enum DormFeeResponsible {
        mother, father, other
    }

    public Long getId() {
        return id;
    }

    public EnrollmentDorm setId(Long id) {
        this.id = id;
        return this;
    }

    public EnrollmentApplication getApplication() {
        return application;
    }

    public EnrollmentDorm setApplication(EnrollmentApplication application) {
        this.application = application;
        return this;
    }

    public DormFeeResponsible getDormFeeResponsible() {
        return dormFeeResponsible;
    }

    public EnrollmentDorm setDormFeeResponsible(DormFeeResponsible dormFeeResponsible) {
        this.dormFeeResponsible = dormFeeResponsible;
        return this;
    }

    public String getOtherName() {
        return otherName;
    }

    public EnrollmentDorm setOtherName(String otherName) {
        this.otherName = otherName;
        return this;
    }

    public String getOtherRelationship() {
        return otherRelationship;
    }

    public EnrollmentDorm setOtherRelationship(String otherRelationship) {
        this.otherRelationship = otherRelationship;
        return this;
    }

    public String getOtherAddress() {
        return otherAddress;
    }

    public EnrollmentDorm setOtherAddress(String otherAddress) {
        this.otherAddress = otherAddress;
        return this;
    }

    public Boolean getScholarshipSupport() {
        return scholarshipSupport;
    }

    public EnrollmentDorm setScholarshipSupport(Boolean scholarshipSupport) {
        this.scholarshipSupport = scholarshipSupport;
        return this;
    }

    public String getScholarshipType() {
        return scholarshipType;
    }

    public EnrollmentDorm setScholarshipType(String scholarshipType) {
        this.scholarshipType = scholarshipType;
        return this;
    }

    public String getAnotherScholarship() {
        return anotherScholarship;
    }

    public EnrollmentDorm setAnotherScholarship(String anotherScholarship) {
        this.anotherScholarship = anotherScholarship;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public EnrollmentDorm setStatus(Status status) {
        this.status = status;
        return this;
    }

    
}
