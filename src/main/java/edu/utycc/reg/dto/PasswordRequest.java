package edu.utycc.reg.dto;

public class PasswordRequest {
    private String oldPassword;
    private String newPassword;
    
    public String getOldPassword() {
        return oldPassword;
    }
    public PasswordRequest setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public PasswordRequest setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    
}

