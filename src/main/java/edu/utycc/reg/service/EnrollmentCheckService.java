package edu.utycc.reg.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import edu.utycc.reg.entity.EnrollmentAdminSetting;
import edu.utycc.reg.repository.EnrollmentAdminSettingRepository;

@Service
public class EnrollmentCheckService {

    private final EnrollmentAdminSettingRepository enrollmentAdminSettingRepository;

    public EnrollmentCheckService(EnrollmentAdminSettingRepository enrollmentAdminSettingRepository) {
        this.enrollmentAdminSettingRepository = enrollmentAdminSettingRepository;
    }

    public boolean isEnrollmentOpen() {
        EnrollmentAdminSetting setting = enrollmentAdminSettingRepository.findById(1L)
                .orElse(null);

        if (setting == null || setting.getEnrollmentOpen() == null || !setting.getEnrollmentOpen()) {
            return false; // Enrollment is closed
        }

        LocalDate today = LocalDate.now();
        LocalDate start = setting.getEnrollmentStart();
        LocalDate end = setting.getEnrollmentEnd();

        return (start != null && end != null && !today.isBefore(start) && !today.isAfter(end));
    }
}
