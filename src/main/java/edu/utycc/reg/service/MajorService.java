package edu.utycc.reg.service;

import org.springframework.stereotype.Service;

import edu.utycc.reg.entity.Major;
import edu.utycc.reg.repository.MajorRepository;

import java.util.List;

@Service
public class MajorService {

    private final MajorRepository majorRepository;

    public MajorService(MajorRepository majorRepository) {
        this.majorRepository = majorRepository;
    }

    public List<Major> getAllMajors() {
        return majorRepository.findAll();
    }

    public Major getMajorById(Short id) {
        System.err.print(id);
        return majorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Major not found with id " + id));
    }
}

