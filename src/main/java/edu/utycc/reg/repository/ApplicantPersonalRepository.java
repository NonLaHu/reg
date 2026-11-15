package edu.utycc.reg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utycc.reg.entity.ApplicantPersonal;
import edu.utycc.reg.entity.EnrollmentApplication;

@Repository
public interface ApplicantPersonalRepository extends JpaRepository<ApplicantPersonal, Long> {
    Optional<ApplicantPersonal> findByApplication(EnrollmentApplication application);
}
