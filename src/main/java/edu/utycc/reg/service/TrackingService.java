package edu.utycc.reg.service;

import org.springframework.stereotype.Service;

import edu.utycc.reg.entity.PreEnrollment;
import edu.utycc.reg.repository.PreEnrollmentRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TrackingService {

    private final PreEnrollmentRepository preEnrollmentRepository;

    public TrackingService(PreEnrollmentRepository preEnrollmentRepository) {
        this.preEnrollmentRepository = preEnrollmentRepository;
    }

    public PreEnrollment checkByPin(String pin) {
        return preEnrollmentRepository.findByTrackingPin(pin)
                .orElseThrow(() -> new EntityNotFoundException("Invalid tracking pin"));
    }
}
