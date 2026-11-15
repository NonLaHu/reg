package edu.utycc.reg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utycc.reg.entity.PreEnrollmentOldStudent;
import edu.utycc.reg.entity.PreEnrollmentOldStudent.Status;;

@Repository
public interface PreEnrollmentOldStudentRepository extends JpaRepository<PreEnrollmentOldStudent, Long> {

    // Example: prevent duplicate NRC (if not already handled at DB constraint level)
    Optional<PreEnrollmentOldStudent> findByNrc(String nrc);

    // Example: find by email
    Optional<PreEnrollmentOldStudent> findByEmail(String email);

    // Example: find by status
    List<PreEnrollmentOldStudent> findByStatus(Status status);

    Page<PreEnrollmentOldStudent> findByFullNameMmContainingIgnoreCase(String fullName, Pageable pageable);

    Page<PreEnrollmentOldStudent> findByStatus(Status status, Pageable pageable);

    Page<PreEnrollmentOldStudent> findByFullNameMmContainingIgnoreCaseAndStatus(
        String fullName, Status status, Pageable pageable
    );

}
