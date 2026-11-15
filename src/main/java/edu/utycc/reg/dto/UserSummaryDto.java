package edu.utycc.reg.dto;

public class UserSummaryDto {
    private Long id;
    private String fullName;
    private String email;
    
    public Long getId() {
        return id;
    }
    public UserSummaryDto setId(Long id) {
        this.id = id;
        return this;
    }
    public String getEmail() {
        return email;
    }
    public UserSummaryDto setEmail(String email) {
        this.email = email;
        return this;
    }
    public String getFullName() {
        return fullName;
    }
    public UserSummaryDto setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }
    
    
}