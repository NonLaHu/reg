package edu.utycc.reg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utycc.reg.entity.ApplicantParent;
import edu.utycc.reg.entity.ApplicantParentNrc;

@Repository
public interface ApplicantParentNrcRepository extends JpaRepository<ApplicantParentNrc, Long> {
    Optional<ApplicantParentNrc> findByApplicantParent(ApplicantParent parent);
}