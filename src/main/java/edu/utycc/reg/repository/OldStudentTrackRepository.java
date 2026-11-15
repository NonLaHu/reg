package edu.utycc.reg.repository;

import edu.utycc.reg.entity.OldStudentTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OldStudentTrackRepository extends JpaRepository<OldStudentTrack, Long> {
    OldStudentTrack findByOldStudent_Id(Long studentId);

}
