package edu.utycc.reg.repository;

import edu.utycc.reg.entity.NrcType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NrcTypeRepository extends JpaRepository<NrcType, Long> {
}
