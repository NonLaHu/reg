package edu.utycc.reg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.utycc.reg.entity.PaymentAdminSetting;

public interface PaymentAdminSettingRepository extends JpaRepository<PaymentAdminSetting, Long> {
    
}
