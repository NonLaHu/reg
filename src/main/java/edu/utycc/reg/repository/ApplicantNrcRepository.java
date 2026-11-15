package edu.utycc.reg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utycc.reg.entity.ApplicantNrc;
import edu.utycc.reg.entity.EnrollmentApplication;

@Repository
public interface ApplicantNrcRepository extends JpaRepository<ApplicantNrc, Long> {
    Optional<ApplicantNrc> findByApplication(EnrollmentApplication application);
}