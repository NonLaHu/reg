package edu.utycc.reg.repository;

import edu.utycc.reg.entity.PreEnrollment;
import edu.utycc.reg.entity.PreEnrollment.Status;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreEnrollmentRepository extends JpaRepository<PreEnrollment, Long> {

    Optional<PreEnrollment> findByEmail(String email);

    Optional<PreEnrollment> findByNrc(String nrc);

    boolean existsByEmail(String email);

    boolean existsByNrc(String nrc);

    List<PreEnrollment> findAllByOrderByCreatedAtDesc();

    long countByStatus(Status status);

    Page<PreEnrollment> findByFullNameMmContainingIgnoreCase(
        String fullNameMm,
        Pageable pageable
    );

    Page<PreEnrollment> findByStatus(Status status, Pageable pageable);

    Page<PreEnrollment> findByFullNameMmContainingIgnoreCaseAndStatus(
        String fullName, Status status, Pageable pageable
    );

    Optional<PreEnrollment> findByTrackingPin(String trackingPin);

    boolean existsByTrackingPin(String trackingPin);
}
