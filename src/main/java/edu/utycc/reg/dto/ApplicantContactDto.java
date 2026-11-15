package edu.utycc.reg.dto;

public class ApplicantContactDto {
    private String phoneNumber;
    private String usualPhoneNumber;
    private String email;
    private String birthPlace;
    private String usualAddressRegion;
    private String usualAddressTownship;
    private String fullAddress;
    private Long id;

    public Long getId() {
        return id;
    }
    public ApplicantContactDto setId(Long id) {
        this.id = id;
        return this;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public ApplicantContactDto setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
    public String getUsualPhoneNumber() {
        return usualPhoneNumber;
    }
    public ApplicantContactDto setUsualPhoneNumber(String usualPhoneNumber) {
        this.usualPhoneNumber = usualPhoneNumber;
        return this;
    }
    public String getEmail() {
        return email;
    }
    public ApplicantContactDto setEmail(String email) {
        this.email = email;
        return this;
    }
    public String getBirthPlace() {
        return birthPlace;
    }
    public ApplicantContactDto setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
        return this;
    }
    public String getUsualAddressRegion() {
        return usualAddressRegion;
    }
    public ApplicantContactDto setUsualAddressRegion(String usualAddressRegion) {
        this.usualAddressRegion = usualAddressRegion;
        return this;
    }
    public String getUsualAddressTownship() {
        return usualAddressTownship;
    }
    public ApplicantContactDto setUsualAddressTownship(String usualAddressTownship) {
        this.usualAddressTownship = usualAddressTownship;
        return this;
    }
    public String getFullAddress() {
        return fullAddress;
    }
    public ApplicantContactDto setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
        return this;
    }

    
    
}
