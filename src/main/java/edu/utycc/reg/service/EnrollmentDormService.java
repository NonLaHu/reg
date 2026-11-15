package edu.utycc.reg.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import edu.utycc.reg.entity.EnrollmentDorm;
import edu.utycc.reg.entity.EnrollmentDorm.Status;
import edu.utycc.reg.repository.EnrollmentDormRepository;
import jakarta.transaction.Transactional;

@Service
public class EnrollmentDormService {

    private final EnrollmentDormRepository dormRepository;

    public EnrollmentDormService(EnrollmentDormRepository dormRepository) {
        this.dormRepository = dormRepository;
    }

    @Transactional
    public Map<String, String> acceptDorm(Long dormId) {
        EnrollmentDorm dorm = dormRepository.findById(dormId)
                .orElseThrow(() -> new IllegalArgumentException("Dorm not found: " + dormId));

        if (dorm.getStatus() == Status.ACCEPTED) {
            throw new IllegalStateException("Dorm already accepted");
        } else if (dorm.getStatus() == Status.REJECTED) {
            throw new IllegalStateException("Dorm already rejected");
        }

        dorm.setStatus(Status.ACCEPTED);
        dormRepository.save(dorm);

        return Map.of("status", dorm.getStatus().name(), "reason", dorm.getOtherName());
    }

    @Transactional
    public Map<String, String> rejectDorm(Long dormId, String reason) {
        EnrollmentDorm dorm = dormRepository.findById(dormId)
                .orElseThrow(() -> new IllegalArgumentException("Dorm not found: " + dormId));

        if (dorm.getStatus() == Status.ACCEPTED) {
            throw new IllegalStateException("Dorm already accepted");
        } else if (dorm.getStatus() == Status.REJECTED) {
            throw new IllegalStateException("Dorm already rejected");
        }

        dorm.setStatus(Status.REJECTED);
        dorm.setOtherAddress(reason); // optionally store reason in remarks-like field
        dormRepository.save(dorm);

        return Map.of("status", dorm.getStatus().name(), "reason", reason);
    }
}
