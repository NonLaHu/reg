package edu.utycc.reg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "applicant_parents_contact")
public class ApplicantParentContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "applicant_parent_id", nullable = false)
    private ApplicantParent applicantParent;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "usual_address_region")
    private NrcState usualAddressRegion;

    @ManyToOne
    @JoinColumn(name = "usual_address_township")
    private NrcTownship usualAddressTownship;

    @Column(name = "full_address", columnDefinition = "TEXT")
    private String fullAddress;

    public Long getId() {
        return id;
    }

    public ApplicantParentContact setId(Long id) {
        this.id = id;
        return this;
    }

    public ApplicantParent getApplicantParent() {
        return applicantParent;
    }

    public ApplicantParentContact setApplicantParent(ApplicantParent applicantParent) {
        this.applicantParent = applicantParent;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ApplicantParentContact setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public NrcState getUsualAddressRegion() {
        return usualAddressRegion;
    }

    public ApplicantParentContact setUsualAddressRegion(NrcState usualAddressRegion) {
        this.usualAddressRegion = usualAddressRegion;
        return this;
    }

    public NrcTownship getUsualAddressTownship() {
        return usualAddressTownship;
    }

    public ApplicantParentContact setUsualAddressTownship(NrcTownship usualAddressTownship) {
        this.usualAddressTownship = usualAddressTownship;
        return this;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public ApplicantParentContact setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
        return this;
    }

    
}
