package edu.utycc.reg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import edu.utycc.reg.entity.EnrollmentApplication;
import edu.utycc.reg.entity.EnrollmentApplication.Status;
import edu.utycc.reg.entity.Student;

@Repository
public interface EnrollmentApplicationRepository extends JpaRepository<EnrollmentApplication, Long> {
    List<EnrollmentApplication> findByStudentId(Student student); 

    Page<EnrollmentApplication> findByStudentId_NameContainingIgnoreCase(String name, Pageable pageable);

    Page<EnrollmentApplication> findByStatus(EnrollmentApplication.Status status, Pageable pageable);

    Page<EnrollmentApplication> findByStudentId_NameContainingIgnoreCaseAndStatus(
        String name, EnrollmentApplication.Status status, Pageable pageable
    );

    List<EnrollmentApplication> findByStudentIdOrderByCreatedAtDesc(Student student);

    long countByStatus(Status status);

}
