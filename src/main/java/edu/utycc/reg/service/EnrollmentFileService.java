package edu.utycc.reg.service;

import edu.utycc.reg.entity.EnrollmentFile;
import edu.utycc.reg.repository.EnrollmentFileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentFileService {

    private final EnrollmentFileRepository fileRepository;

    public EnrollmentFileService(EnrollmentFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    /**
     * Count total files
     */
    public long countAllFiles() {
        return fileRepository.count();
    }

    /**
     * Optional: Get all files
     */
    public List<EnrollmentFile> getAllFiles() {
        return fileRepository.findAll();
    }
}
