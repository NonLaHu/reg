package edu.utycc.reg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.utycc.reg.entity.Major;

import java.util.Optional;


public interface MajorRepository extends JpaRepository<Major, Long> {
    Optional<Major> findFirstBy(); // convenience

    Optional<Major> findById(Short id);
}