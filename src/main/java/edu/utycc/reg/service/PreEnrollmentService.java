package edu.utycc.reg.service;

import edu.utycc.reg.dto.PreEnrollmentDto;
import edu.utycc.reg.dto.PreEnrollmentSummaryDTO;
import edu.utycc.reg.entity.PreEnrollment;
import edu.utycc.reg.repository.PreEnrollmentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import edu.utycc.reg.entity.PreEnrollment.Status;

@Service
public class PreEnrollmentService {

    private final PreEnrollmentRepository repository;
    private static final String PIN_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final SecureRandom random = new SecureRandom();

    public PreEnrollmentService(PreEnrollmentRepository repository) {
        this.repository = repository;
    }

    // -----------------------
    // CRUD Operations
    // -----------------------

    @Transactional
    public PreEnrollment create(PreEnrollment preEnrollment) {
        preEnrollment.setTrackingPin(generateUniquePin());
        return repository.save(preEnrollment);
    }

    public long countPreEnrollments() {
        return repository.countByStatus(Status.PENDING);
    }

    public List<PreEnrollment> getAll() {
        return repository.findAll();
    }

    public Optional<PreEnrollment> getById(Long id) {
        return repository.findById(id);
    }

    public PreEnrollmentDto getPreEnrollmentById(Long id) {
        PreEnrollment entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PreEnrollment not found"));

        PreEnrollmentDto dto = new PreEnrollmentDto();
        dto.setId(entity.getId());
        dto.setGender(entity.getGender().name());
        dto.setEmail(entity.getEmail());
        dto.setFullNameMm(entity.getFullNameMm());
        dto.setUniEntranceRollno(entity.getUniEntranceRollno());
        dto.setUniEntranceAcademicYear(entity.getUniEntranceAcademicYear());
        dto.setNrc(entity.getNrc());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setViberPhoneNumber(entity.getViberPhoneNumber());
        dto.setStatus(entity.getStatus().name());
        dto.setInformed(entity.getInformed());
        dto.setCreatedAt(entity.getCreatedAt());
        // Map the three new fields
        dto.setRollEntranceOrder(entity.getRollEntranceOrder());
        dto.setFatherName(entity.getFatherName());
        dto.setBirthOfDate(entity.getBirthOfDate() != null ? entity.getBirthOfDate().toString() : null);
        dto.setTrackingPin(entity.getTrackingPin() != null ? entity.getTrackingPin() : null);

        // Build file URL if path exists
        if (entity.getPassCertificate() != null && !entity.getPassCertificate().isEmpty()) {
            dto.setPassCertificateUrl("/files/" + entity.getPassCertificate());
        }

        return dto;
    }

    private String generateUniquePin() {
        int attempts = 0;
        final int maxAttempts = 1000;

        while (attempts < maxAttempts) {
            attempts++;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                sb.append(PIN_CHARS.charAt(random.nextInt(PIN_CHARS.length())));
            }
            String pin = sb.toString();

            if (!repository.existsByTrackingPin(pin)) {
                return pin;
            }
        }

        throw new IllegalStateException("Failed to generate unique tracking pin after " + maxAttempts + " attempts");
    }

    public PreEnrollment update(Long id, PreEnrollment updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setGender(updated.getGender());
                    existing.setEmail(updated.getEmail());
                    existing.setFullNameMm(updated.getFullNameMm());
                    existing.setUniEntranceRollno(updated.getUniEntranceRollno());
                    existing.setUniEntranceAcademicYear(updated.getUniEntranceAcademicYear());
                    existing.setNrc(updated.getNrc());
                    existing.setPassCertificate(updated.getPassCertificate());
                    existing.setStatus(updated.getStatus());
                    existing.setInformed(updated.getInformed());
                    existing.setPhoneNumber(updated.getPhoneNumber());
                    existing.setViberPhoneNumber(updated.getViberPhoneNumber());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("PreEnrollment not found with id " + id));
    }

    public Page<PreEnrollment> getPreEnrollments(String filter, String status, Pageable pageable) {
        PreEnrollment.Status statusEnum = null;
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = PreEnrollment.Status.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // maybe ignore or return empty page
                return Page.empty(pageable);
            }
        }

        boolean hasFilter = filter != null && !filter.isBlank();
        boolean hasStatus = statusEnum != null;

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

    public List<PreEnrollmentSummaryDTO> getRecentPreEnrollments(int limit) {
        return repository.findAllByOrderByCreatedAtDesc()
                .stream()
                .limit(limit)
                .map(PreEnrollmentSummaryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    // -----------------------
    // Custom Actions
    // -----------------------

    public PreEnrollment updateStatus(Long id, PreEnrollment.Status status) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setStatus(status);
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("PreEnrollment not found with id " + id));
    }

    public PreEnrollment markInformed(Long id) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setInformed(true);
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("PreEnrollment not found with id " + id));
    }

    @Transactional
    public void updateField(Long preEnrollmentId, String path, String value) {
        PreEnrollment preEnrollment = repository.findById(preEnrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Pre-enrollment not found"));

        switch (path) {

            // ================= Basic Info =================
            case "fullNameMm" -> preEnrollment.setFullNameMm(value);
            case "fatherName" -> preEnrollment.setFatherName(value);
            case "rollEntranceOrder" -> {
                try {
                    preEnrollment.setRollEntranceOrder(value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid roll number: " + value);
                }
            }

            case "birthOfDate" -> {
                try {
                    // adjust format as needed; JS may send "YYYY/MM/DD"
                    preEnrollment.setBirthOfDate(LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid date format: " + value);
                }
            }

            case "email" -> preEnrollment.setEmail(value);
            case "nrc" -> preEnrollment.setNrc(value);
            case "phoneNumber" -> preEnrollment.setPhoneNumber(value);
            case "viberPhoneNumber" -> preEnrollment.setViberPhoneNumber(value);

            default -> throw new IllegalArgumentException("Unsupported field path: " + path);
        }

        repository.save(preEnrollment);
    }
}
