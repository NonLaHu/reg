package edu.utycc.reg.dto;

public class ApplicantParentContactDto {
    private String phoneNumber;
    private Short usualAddressRegionId;
    private Short usualAddressTownshipId;
    private String fullAddress;
    private Long id;

    public Long getId() {
        return id;
    }
    public ApplicantParentContactDto setId(Long id) {
        this.id = id;
        return this;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public ApplicantParentContactDto setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
    public Short getUsualAddressRegionId() {
        return usualAddressRegionId;
    }
    public ApplicantParentContactDto setUsualAddressRegionId(Short usualAddressRegionId) {
        this.usualAddressRegionId = usualAddressRegionId;
        return this;
    }
    public Short getUsualAddressTownshipId() {
        return usualAddressTownshipId;
    }
    public ApplicantParentContactDto setUsualAddressTownshipId(Short usualAddressTownshipId) {
        this.usualAddressTownshipId = usualAddressTownshipId;
        return this;
    }
    public String getFullAddress() {
        return fullAddress;
    }
    public ApplicantParentContactDto setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
        return this;
    }

    
}
