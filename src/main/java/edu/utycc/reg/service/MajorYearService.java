package edu.utycc.reg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.utycc.reg.entity.MajorYear;
import edu.utycc.reg.repository.MajorYearRepository;

@Service
public class MajorYearService {

    private final MajorYearRepository majorYearRepository;

    public MajorYearService(MajorYearRepository majorYearRepository) {
        this.majorYearRepository = majorYearRepository;
    }

    // Get all years
    public List<MajorYear> findAll() {
        return majorYearRepository.findAll();
    }

    // Get years by major
    public List<MajorYear> findByMajorId(Short majorId) {
        return majorYearRepository.findByMajorId(majorId);
    }

    // Get by id
    public MajorYear findById(Short id) {
        return majorYearRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MajorYear not found with id " + id));
    }

    // Save or update
    public MajorYear save(MajorYear majorYear) {
        return majorYearRepository.save(majorYear);
    }

}
