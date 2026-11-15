package edu.utycc.reg.dto;

public class AcceptResponsePreEnroll {
    private String serialKey;
    private String password;

    public AcceptResponsePreEnroll() {}
    public AcceptResponsePreEnroll(String serialKey, String password) {
        this.serialKey = serialKey;
        this.password = password;
    }
    public String getSerialKey() { return serialKey; }
    public void setSerialKey(String serialKey) { this.serialKey = serialKey; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
