package edu.utycc.reg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utycc.reg.entity.ApplicantParent;
import edu.utycc.reg.entity.ApplicantParentContact;

@Repository
public interface ApplicantParentContactRepository extends JpaRepository<ApplicantParentContact, Long> {
    Optional<ApplicantParentContact> findByApplicantParent(ApplicantParent parent);
}