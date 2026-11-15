package edu.utycc.reg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utycc.reg.entity.Ethnicity;

@Repository
public interface EthnicityRepository extends JpaRepository<Ethnicity, Long> {
}
