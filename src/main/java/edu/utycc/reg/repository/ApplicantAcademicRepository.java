package edu.utycc.reg.repository;

import edu.utycc.reg.entity.ApplicantAcademic;
import edu.utycc.reg.entity.EnrollmentApplication;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantAcademicRepository extends JpaRepository<ApplicantAcademic, Long> {
    Optional<ApplicantAcademic> findByApplication(EnrollmentApplication application);
}
