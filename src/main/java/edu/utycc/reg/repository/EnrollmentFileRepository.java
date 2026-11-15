package edu.utycc.reg.repository;

import edu.utycc.reg.entity.EnrollmentApplication;
import edu.utycc.reg.entity.EnrollmentFile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentFileRepository extends JpaRepository<EnrollmentFile, Long> {
    // You get count() from JpaRepository already
    
    List<EnrollmentFile> findByApplication(EnrollmentApplication application);
}
