package edu.utycc.reg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.utycc.reg.entity.EnrollmentAdminSetting;

public interface EnrollmentAdminSettingRepository extends JpaRepository<EnrollmentAdminSetting, Long> {
}