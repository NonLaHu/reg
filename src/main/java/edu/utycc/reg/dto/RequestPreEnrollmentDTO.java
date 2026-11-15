package edu.utycc.reg.dto;

import edu.utycc.reg.entity.PreEnrollment;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.web.util.HtmlUtils;

public record RequestPreEnrollmentDTO(
        String gender,
        String email,
        String fullNameMm,
        String uniEntranceRollno,
        String uniEntranceAcademicYear,
        String nrc,
        String phoneNumber,
        String viberPhoneNumber,
        String rollEntranceOrder,
        String fatherName,
        String birthOfDate) {
    public PreEnrollment toEntity() {
        PreEnrollment entity = new PreEnrollment();

        entity.setGender(PreEnrollment.Gender.valueOf(gender.toUpperCase()));
        entity.setEmail(email != null ? HtmlUtils.htmlEscape(email) : null);
        entity.setFullNameMm(HtmlUtils.htmlEscape(fullNameMm));
        entity.setUniEntranceRollno(HtmlUtils.htmlEscape(uniEntranceRollno));
        entity.setUniEntranceAcademicYear(HtmlUtils.htmlEscape(uniEntranceAcademicYear));
        entity.setNrc(nrc != null ? HtmlUtils.htmlEscape(nrc) : null);
        entity.setPhoneNumber(HtmlUtils.htmlEscape(phoneNumber));
        entity.setViberPhoneNumber(HtmlUtils.htmlEscape(viberPhoneNumber));
        entity.setRollEntranceOrder(HtmlUtils.htmlEscape(rollEntranceOrder));
        entity.setFatherName(HtmlUtils.htmlEscape(fatherName));
        if (birthOfDate != null && !birthOfDate.isBlank()) {
            try {
                entity.setBirthOfDate(LocalDate.parse(birthOfDate)); // expects "YYYY-MM-DD"
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid birthOfDate format: " + birthOfDate);
            }
        } else {
            entity.setBirthOfDate(null);
        }

        // defaults
        entity.setStatus(PreEnrollment.Status.PENDING);
        entity.setInformed(false);
        return entity;
    }
}
