package edu.utycc.reg.service;

import org.springframework.stereotype.Service;

import edu.utycc.reg.entity.EnrollmentAdminSetting;
import edu.utycc.reg.repository.EnrollmentAdminSettingRepository;

@Service
public class EnrollmentAdminSettingService {

    private final EnrollmentAdminSettingRepository repo;

    public EnrollmentAdminSettingService(EnrollmentAdminSettingRepository repo) {
        this.repo = repo;
    }

    public EnrollmentAdminSetting getSettings() {
        return repo.findById(1L).orElseGet(() -> {
            EnrollmentAdminSetting defaultSetting = new EnrollmentAdminSetting();
            defaultSetting.setId(1L);
            return repo.save(defaultSetting);
        });
    }

    public EnrollmentAdminSetting getEnrollmentSetting() {
        return repo.findById(1L).orElse(null);
    }

    public EnrollmentAdminSetting updateSettings(EnrollmentAdminSetting newSettings) {
        newSettings.setId(1L);
        return repo.save(newSettings);
    }
}
