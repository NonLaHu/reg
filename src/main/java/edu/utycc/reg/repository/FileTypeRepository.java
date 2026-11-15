package edu.utycc.reg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utycc.reg.entity.FileType;

@Repository
public interface FileTypeRepository extends JpaRepository<FileType, Long> {
    Optional<FileType> findById(FileType filetype);
}
