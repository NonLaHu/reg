package edu.utycc.reg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utycc.reg.entity.EnrollmentApplication;
import edu.utycc.reg.entity.EnrollmentMajor;

@Repository
public interface EnrollmentMajorRepository extends JpaRepository<EnrollmentMajor, Long> {
    List<EnrollmentMajor> findByApplication(EnrollmentApplication application);

}