package edu.utycc.reg.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.utycc.reg.entity.PaymentAdminSetting;
import edu.utycc.reg.repository.PaymentAdminSettingRepository;

@Service
public class PaymentAdminSettingService {

    private final PaymentAdminSettingRepository repo;

    public PaymentAdminSettingService(PaymentAdminSettingRepository repo) {
        this.repo = repo;
    }

    public PaymentAdminSetting getSettings() {
        return repo.findById(1L).orElseGet(() -> {
            PaymentAdminSetting defaultSetting = new PaymentAdminSetting();
            defaultSetting.setId(1L);
            return repo.save(defaultSetting);
        });
    }

    public PaymentAdminSetting getPaymentSetting() {
        return repo.findById(1L).orElse(null);
    }

    public PaymentAdminSetting updateSettings(PaymentAdminSetting newSettings) {
        newSettings.setId(1L);
        return repo.save(newSettings);
    }

    @Transactional
    public PaymentAdminSetting updateQrPath(String filePath) {
        PaymentAdminSetting setting = repo.findById(1L).orElseGet(() -> {
            PaymentAdminSetting s = new PaymentAdminSetting();
            s.setId(1L);
            return s;
        });
        setting.setPaymentQrPath(filePath);
        return repo.save(setting);
    }
}
