package edu.utycc.reg.dto;

import edu.utycc.reg.entity.PreEnrollment;
import java.time.LocalDateTime;

public record PreEnrollmentSummaryDTO(
        Long id,
        String fullNameMm,
        String phoneNumber,
        String viberPhoneNumber,
        String status,
        LocalDateTime createdAt
) {
    public static PreEnrollmentSummaryDTO fromEntity(PreEnrollment entity) {
        return new PreEnrollmentSummaryDTO(
                entity.getId(),
                entity.getFullNameMm(),
                entity.getPhoneNumber(),
                entity.getViberPhoneNumber(),
                entity.getStatus() != null ? entity.getStatus().name() : "UNKNOWN",
                entity.getCreatedAt()
        );
    }
}
