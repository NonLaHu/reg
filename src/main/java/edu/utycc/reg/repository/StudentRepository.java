package edu.utycc.reg.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import edu.utycc.reg.entity.Student;
import edu.utycc.reg.entity.User;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);

    Optional<Student> findByUserId(Long userId);

    Page<Student> findByNameContainingIgnoreCaseOrRollNoContainingIgnoreCase(
        String name,
        String rollNo,
        Pageable pageable
    );

    @EntityGraph(attributePaths = {"preEnrollment", "major", "semester", "user"})
    Optional<Student> findById(Long id);

    @EntityGraph(attributePaths = {"preEnrollment", "major", "semester", "user"})
    Optional<Student> findByRollNo(String rollNo);

}