package edu.utycc.reg.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "applicant_contact")
public class ApplicantContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false)
    private EnrollmentApplication application;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "usual_phone_number", length = 20)
    private String usualPhoneNumber;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "birth_place", length = 255)
    private String birthPlace;

    @Column(name = "usual_address_region", length = 100)
    private String usualAddressRegion;

    @Column(name = "usual_address_township", length = 100)
    private String usualAddressTownship;

    @Column(columnDefinition = "TEXT")
    private String fullAddress;

    public Long getId() {
        return id;
    }

    public ApplicantContact setId(Long id) {
        this.id = id;
        return this;
    }

    public EnrollmentApplication getApplication() {
        return application;
    }

    public ApplicantContact setApplication(EnrollmentApplication application) {
        this.application = application;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ApplicantContact setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getUsualPhoneNumber() {
        return usualPhoneNumber;
    }

    public ApplicantContact setUsualPhoneNumber(String usualPhoneNumber) {
        this.usualPhoneNumber = usualPhoneNumber;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ApplicantContact setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public ApplicantContact setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
        return this;
    }

    public String getUsualAddressRegion() {
        return usualAddressRegion;
    }

    public ApplicantContact setUsualAddressRegion(String usualAddressRegion) {
        this.usualAddressRegion = usualAddressRegion;
        return this;
    }

    public String getUsualAddressTownship() {
        return usualAddressTownship;
    }

    public ApplicantContact setUsualAddressTownship(String usualAddressTownship) {
        this.usualAddressTownship = usualAddressTownship;
        return this;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public ApplicantContact setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
        return this;
    }

    
}
