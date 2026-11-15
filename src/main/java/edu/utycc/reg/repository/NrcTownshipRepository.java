package edu.utycc.reg.repository;

import edu.utycc.reg.entity.NrcTownship;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NrcTownshipRepository extends JpaRepository<NrcTownship, Long> {
     List<NrcTownship> findByStateCode(String stateCode);
     List<NrcTownship> findByStateId(Short stateId);
}
