package edu.utycc.reg.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import edu.utycc.reg.entity.MajorYear;

import java.util.List;
import java.util.Optional;

public interface MajorYearRepository extends JpaRepository<MajorYear, Long> {
    Optional<MajorYear> findFirstBy(); // convenience

    List<MajorYear> findByMajorId(Short majorId);

    Optional<MajorYear> findById(Short id);
}