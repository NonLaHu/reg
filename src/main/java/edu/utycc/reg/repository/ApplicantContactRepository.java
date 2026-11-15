package edu.utycc.reg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utycc.reg.entity.ApplicantContact;
import edu.utycc.reg.entity.EnrollmentApplication;

@Repository
public interface ApplicantContactRepository extends JpaRepository<ApplicantContact, Long> {
    Optional<ApplicantContact> findByApplication(EnrollmentApplication application);
}