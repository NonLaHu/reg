package edu.utycc.reg.dto;

public class LoginResponse {
    private String token;

    private long expiresIn;

    private boolean Admin;

    public String getToken() {
        return token;
    }

    public LoginResponse setToken(String token) { 
        this.token = token;
        return this;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
   }

    public boolean isAdmin() {
        return Admin;
    }

    public LoginResponse setAdmin(boolean admin) {
        Admin = admin;
        return this;
    }

   
}