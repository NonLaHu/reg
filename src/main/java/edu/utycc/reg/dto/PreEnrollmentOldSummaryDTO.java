package edu.utycc.reg.dto;

import java.time.LocalDateTime;

import edu.utycc.reg.entity.PreEnrollmentOldStudent;

public record PreEnrollmentOldSummaryDTO (
        Long id,
        String fullNameMm,
        String phoneNumber,
        String viberPhoneNumber,
        String status,
        LocalDateTime createdAt
) {
    public static PreEnrollmentOldSummaryDTO fromEntity(PreEnrollmentOldStudent entity) {
        return new PreEnrollmentOldSummaryDTO(
                entity.getId(),
                entity.getFullNameMm(),
                entity.getPhoneNumber(),
                entity.getViberPhoneNumber(),
                entity.getStatus() != null ? entity.getStatus().name() : "UNKNOWN",
                entity.getCreatedAt()
        );
    }
}
