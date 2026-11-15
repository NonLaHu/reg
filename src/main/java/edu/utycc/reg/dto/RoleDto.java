package edu.utycc.reg.dto;

import edu.utycc.reg.entity.Role;

public class RoleDto {

    private Long id;
    private String name;
    private String description;

    public RoleDto() {
    }

    public RoleDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static RoleDto fromEntity(Role role) {
        if (role == null) return null;
        return new RoleDto(role.getId(), role.getName(), role.getDescription());
    }

    public Long getId() {
        return id;
    }

    public RoleDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public RoleDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RoleDto setDescription(String description) {
        this.description = description;
        return this;
    }
}
