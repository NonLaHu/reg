package edu.utycc.reg.repository;

import edu.utycc.reg.entity.NrcState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NrcStateRepository extends JpaRepository<NrcState, Long> {
    // You get count() from JpaRepository already
}
