package edu.utycc.reg.dto;

import java.time.LocalDateTime;

public record RecentUserDTO(
        Long id,
        String fullName,
        String email,
        String role,
        LocalDateTime joined,
        boolean enabled
) {

    public static RecentUserDTO fromEntity(edu.utycc.reg.entity.User user) {
        return new RecentUserDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getUserType() != null ? user.getUserType().name() : "USER",
                user.getCreatedAt(),
                user.getStatus() == edu.utycc.reg.entity.User.Status.ACTIVE
        );
    }
}
