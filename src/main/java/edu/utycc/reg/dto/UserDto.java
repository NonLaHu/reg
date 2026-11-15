package edu.utycc.reg.dto;

import edu.utycc.reg.entity.User;
import edu.utycc.reg.entity.User.Status;
import edu.utycc.reg.entity.User.UserType;

import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.Set;

public class UserDto {
    private Long id;
    private String email;
    private String fullName;
    private UserType userType;
    private Status status;
    private Set<String> roles;
    private LocalDateTime createdAt;

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.userType = user.getUserType();
        this.status = user.getStatus();
        this.createdAt = user.getCreatedAt();
        this.roles = user.getRoles()
                            .stream()
                            .map(role -> role.getName())
                            .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
