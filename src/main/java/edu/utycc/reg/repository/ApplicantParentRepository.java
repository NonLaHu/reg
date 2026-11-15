package edu.utycc.reg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utycc.reg.entity.ApplicantParent;
import edu.utycc.reg.entity.EnrollmentApplication;

@Repository
public interface ApplicantParentRepository extends JpaRepository<ApplicantParent, Long> {
    List<ApplicantParent> findByApplication(EnrollmentApplication application);
}