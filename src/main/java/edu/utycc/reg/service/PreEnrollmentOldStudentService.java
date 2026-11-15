package edu.utycc.reg.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.utycc.reg.dto.PreEnrollmentOldDTO;
import edu.utycc.reg.dto.RequestPreEnrollmentOldDTO;
import edu.utycc.reg.entity.Major;
import edu.utycc.reg.entity.MajorYear;
import edu.utycc.reg.entity.PreEnrollment;
import edu.utycc.reg.entity.PreEnrollmentOldStudent;
import edu.utycc.reg.repository.PreEnrollmentOldStudentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class PreEnrollmentOldStudentService {
    private final PreEnrollmentOldStudentRepository repository;
    private final MajorService majorService;
    private final MajorYearService majorYearService;

    public PreEnrollmentOldStudentService(
            PreEnrollmentOldStudentRepository repository,
            MajorService majorService,
            MajorYearService majorYearService) {
        this.repository = repository;
        this.majorService = majorService;
        this.majorYearService = majorYearService;
    }

    public Page<PreEnrollmentOldStudent> getPreEnrollments(String filter, String status, Pageable pageable) {
        PreEnrollmentOldStudent.Status statusEnum = null;

        // Convert string to enum if provided
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = PreEnrollmentOldStudent.Status.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return Page.empty(pageable);
            }
        }

        boolean hasFilter = filter != null && !filter.isBlank();
        boolean hasStatus = statusEnum != null;

        // Branch based on which filters are present
        if (hasFilter && hasStatus) {
            return repository.findByFullNameMmContainingIgnoreCaseAndStatus(filter, statusEnum, pageable);
        } else if (hasFilter) {
            return repository.findByFullNameMmContainingIgnoreCase(filter, pageable);
        } else if (hasStatus) {
            return repository.findByStatus(statusEnum, pageable);
        } else {
            return repository.findAll(pageable);
        }
    }

    public PreEnrollmentOldStudent create(RequestPreEnrollmentOldDTO dto, String studentIdCardUrl) {
        Major major = majorService.getMajorById(dto.getMajorId());
        MajorYear majorYear = majorYearService.findById(dto.getMajorYearId());

        PreEnrollmentOldStudent entity = new PreEnrollmentOldStudent();
        entity.setGender(dto.getGender());
        entity.setFullNameMm(dto.getFullNameMm());
        entity.setPrevRollNo(dto.getPrevRollNo());
        entity.setEmail(dto.getEmail());
        entity.setNrc(dto.getNrc());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setViberPhoneNumber(dto.getViberPhoneNumber());
        entity.setStudentIdCardUrl(studentIdCardUrl);
        entity.setMajor(major);
        entity.setMajorYear(majorYear);

        return repository.save(entity);
    }

    public PreEnrollmentOldDTO getPreEnrollmentOldById(Long id) {
        PreEnrollmentOldStudent entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PreEnrollment (old) not found with id " + id));

        PreEnrollmentOldDTO dto = new PreEnrollmentOldDTO();
        dto.setId(entity.getId());
        dto.setGender(entity.getGender());
        dto.setEmail(entity.getEmail());
        dto.setFullNameMm(entity.getFullNameMm());
        dto.setPrevRollNo(entity.getPrevRollNo());
        dto.setNrc(entity.getNrc());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setViberPhoneNumber(entity.getViberPhoneNumber());
        dto.setStatus(entity.getStatus());
        dto.setInformed(entity.getInformed());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setRemarks(entity.getRemarks());

        dto.setMajor(entity.getMajor().getName()); // assuming Major has getName()
        dto.setMajorYear(entity.getMajorYear().getYearLabel()); // assuming MajorYear has getName()

        // Build file URL if studentIdCard exists
        if (entity.getStudentIdCardUrl() != null && !entity.getStudentIdCardUrl().isEmpty()) {
            dto.setStudentIdCardUrl("/files/" + entity.getStudentIdCardUrl());
        }

        return dto;
    }

    @Transactional
    public void updateField(Long preEnrollmentId, String path, String value) {
        PreEnrollmentOldStudent preEnrollment = repository.findById(preEnrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Pre-enrollment not found"));

        switch (path) {

            // ================= Basic Info =================
            case "fullNameMm" -> preEnrollment.setFullNameMm(value);
            case "email" -> preEnrollment.setEmail(value);
            case "prevRollNo" -> preEnrollment.setPrevRollNo(value);

            case "phoneNumber" -> preEnrollment.setPhoneNumber(value);
            case "viberPhoneNumber" -> preEnrollment.setViberPhoneNumber(value);

            // ================= Major Info =================
            case "major" -> {
                try {
                    Major major = majorService.getMajorById(Short.parseShort(value));
                    preEnrollment.setMajor(major);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid major id: " + value);
                }
            }

            case "majorYear" -> {
                try {
                    MajorYear majorYear = majorYearService.findById(Short.parseShort(value));
                    preEnrollment.setMajorYear(majorYear);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid majorYear id: " + value);
                }
            }
            
            default -> throw new IllegalArgumentException("Unsupported field path: " + path);
        }

        repository.save(preEnrollment);
    }

}
