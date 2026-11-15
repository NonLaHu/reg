package edu.utycc.reg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utycc.reg.entity.EnrollmentApplication;
import edu.utycc.reg.entity.EnrollmentDorm;

@Repository
public interface EnrollmentDormRepository extends JpaRepository<EnrollmentDorm, Long> {
    Optional<EnrollmentDorm> findByApplication(EnrollmentApplication application);
    Optional<EnrollmentDorm> findByApplicationId(Long applicationId);
}