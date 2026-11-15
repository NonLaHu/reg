package edu.utycc.reg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utycc.reg.entity.ApplicationApproval;
import edu.utycc.reg.entity.EnrollmentApplication;

@Repository
public interface ApplicationApprovalRepository extends JpaRepository<ApplicationApproval, Long> {
    List<ApplicationApproval> findByApplication(EnrollmentApplication application);

}