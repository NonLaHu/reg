package edu.utycc.reg.service;

import edu.utycc.reg.dto.*;
import edu.utycc.reg.entity.*;
import edu.utycc.reg.repository.*;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EnrollmentService {
    private static final Logger log = LoggerFactory.getLogger(EnrollmentService.class);
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentApplicationRepository applicationRepository;
    private final ApplicantPersonalRepository personalRepository;
    private final ApplicantContactRepository contactRepository;
    private final ApplicantNrcRepository nrcRepository;
    private final ApplicantParentRepository parentRepository;
    private final ApplicantParentContactRepository parentContactRepository;
    private final ApplicantParentNrcRepository parentNrcRepository;
    private final ApplicantAcademicRepository academicRepository;
    private final EnrollmentMajorRepository enrollmentMajorRepository;
    private final EnrollmentDormRepository dormRepository;
    private final EnrollmentFileRepository fileRepository;
    private final MajorRepository majorRepository;
    private final MajorYearRepository majorYearRepository;
    private final Path uploadBase;

    public EnrollmentService(UserRepository userRepository,
            StudentRepository studentRepository,
            EnrollmentApplicationRepository applicationRepository,
            ApplicantPersonalRepository personalRepository,
            @Value("${app.upload.dir:uploads}") String uploadDir,
            ApplicantContactRepository contactRepository,
            ApplicantNrcRepository nrcRepository,
            ApplicantParentRepository parentRepository,
            ApplicantParentContactRepository parentContactRepository,
            ApplicantParentNrcRepository parentNrcRepository,
            ApplicantAcademicRepository academicRepository,
            EnrollmentMajorRepository enrollmentMajorRepository,
            EnrollmentDormRepository dormRepository,
            EnrollmentFileRepository fileRepository,
            MajorYearRepository majorYearRepository,
            MajorRepository majorRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.applicationRepository = applicationRepository;
        this.personalRepository = personalRepository;
        this.contactRepository = contactRepository;
        this.nrcRepository = nrcRepository;
        this.parentRepository = parentRepository;
        this.parentContactRepository = parentContactRepository;
        this.parentNrcRepository = parentNrcRepository;
        this.academicRepository = academicRepository;
        this.enrollmentMajorRepository = enrollmentMajorRepository;
        this.dormRepository = dormRepository;
        this.fileRepository = fileRepository;
        this.majorYearRepository = majorYearRepository;
        this.majorRepository = majorRepository;
        this.uploadBase = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadBase);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not create upload directory", ex);
        }
    }

    @Transactional
    public void acceptEnrollment(EnrollmentApplicationDto dto,
            List<EnrollmentRequestFresher.FileEntry> files, // FIXED: files param (JSON file entries) preferred
            String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        // Expect only students to enroll
        if (user.getUserType() != User.UserType.STUDENT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only students can create enrollment applications");
        }

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student record not found"));

        if (student.isEnrolled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student already enrolled");
        }

        // Create EnrollmentApplication
        EnrollmentApplication application = new EnrollmentApplication();
        application.setIsFresher(Boolean.TRUE);
        application.setCreatedAt(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());
        application.setStatus(EnrollmentApplication.Status.PENDING);

        // link student
        application.setStudentId(student);

        // persist application first so we have id
        EnrollmentApplication savedApplication = applicationRepository.save(application);

        // ApplicantPersonal
        if (dto.getPersonal() != null) {
            ApplicantPersonalDto p = dto.getPersonal();
            ApplicantPersonal pers = new ApplicantPersonal();
            pers.setApplication(savedApplication);
            pers.setFullNameMm(p.getFullNameMm());
            pers.setFullNameEn(p.getFullNameEn());
            try {
                pers.setGender(ApplicantPersonal.Gender.valueOf(p.getGender()));
            } catch (Exception ignore) {
            }
            pers.setDateOfBirth(p.getDateOfBirth());
            pers.setEthnic(p.getEthnic());
            pers.setReligion(p.getReligion());
            personalRepository.save(pers);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The form was incomplete");
        }

        // ApplicantContact
        if (dto.getContact() != null) {
            ApplicantContactDto c = dto.getContact();
            ApplicantContact contact = new ApplicantContact();
            contact.setApplication(savedApplication);
            contact.setPhoneNumber(c.getPhoneNumber());
            contact.setUsualPhoneNumber(c.getUsualPhoneNumber());
            contact.setEmail(c.getEmail());
            contact.setBirthPlace(c.getBirthPlace());
            contact.setUsualAddressRegion(c.getUsualAddressRegion());
            contact.setUsualAddressTownship(c.getUsualAddressTownship());
            contact.setFullAddress(c.getFullAddress());
            contactRepository.save(contact);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The form was incomplete");
        }

        // ApplicantNrc
        if (dto.getNrc() != null) {
            ApplicantNrcDto n = dto.getNrc();
            ApplicantNrc anrc = new ApplicantNrc();
            anrc.setApplication(savedApplication);
            try {
                anrc.setNrcStatus(ApplicantNrc.NrcStatus.valueOf(n.getNrcStatus()));
            } catch (Exception ignore) {
            }
            anrc.setNrc(n.getNrc());
            anrc.setNationalNrc(n.getNationalNrc());
            nrcRepository.save(anrc);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The form was incomplete");
        }

        // Academics
        if (dto.getAcademics() != null) {
            ApplicantAcademicDto acadDto = dto.getAcademics();
            ApplicantAcademic acad = new ApplicantAcademic();
            acad.setApplication(savedApplication);
            acad.setAcademicYear(acadDto.getAcademicYear());
            acad.setUniEntranceRollno(acadDto.getUniEntranceRollno());
            acad.setUniEntranceYear(acadDto.getUniEntranceYear());
            if (acadDto.getMajorYearId() != null) {
                MajorYear my = new MajorYear();
                my.setId(acadDto.getMajorYearId());
                acad.setMajorYear(my);
            }
            acad.setRollNo(acadDto.getRollNo());
            academicRepository.save(acad);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The form was incomplete");
        }

        // Parents + their contacts + nrcs
        if (dto.getParents() != null) {
            for (ApplicantParentDto parentDto : dto.getParents()) {
                ApplicantParent parent = new ApplicantParent();
                parent.setApplication(savedApplication);
                try {
                    parent.setParentType(ApplicantParent.ParentType.valueOf(parentDto.getParentType()));
                } catch (Exception ignore) {
                }
                parent.setFullNameMm(parentDto.getFullNameMm());
                parent.setFullNameEn(parentDto.getFullNameEn());
                parent.setEthnic(parentDto.getEthnic());
                parent.setReligion(parentDto.getReligion());
                parent.setOccupation(parentDto.getOccupation());
                parent.setDateOfBirth(parentDto.getDateOfBirth());
                parent.setIsDeceased(parentDto.getIsDeceased() != null ? parentDto.getIsDeceased() : Boolean.FALSE);
                parent.setDateOfDeath(parentDto.getDateOfDeath());
                ApplicantParent savedParent = parentRepository.save(parent);

                if (parentDto.getContacts() != null) {
                    ApplicantParentContactDto pcDto = parentDto.getContacts();
                    ApplicantParentContact pc = new ApplicantParentContact();
                    pc.setApplicantParent(savedParent);
                    pc.setPhoneNumber(pcDto.getPhoneNumber());
                    if (pcDto.getUsualAddressRegionId() != null) {
                        NrcState ns = new NrcState();
                        ns.setId(pcDto.getUsualAddressRegionId());
                        pc.setUsualAddressRegion(ns);
                    }
                    if (pcDto.getUsualAddressTownshipId() != null) {
                        NrcTownship nt = new NrcTownship();
                        nt.setId(pcDto.getUsualAddressTownshipId());
                        pc.setUsualAddressTownship(nt);
                    }
                    pc.setFullAddress(pcDto.getFullAddress());
                    parentContactRepository.save(pc);

                }

                if (parentDto.getNrcs() != null) {
                    ApplicantParentNrcDto pnDto = parentDto.getNrcs();
                    ApplicantParentNrc pn = new ApplicantParentNrc();
                    pn.setApplicantParent(savedParent);
                    try {
                        pn.setNrcStatus(ApplicantParentNrc.NrcStatus.valueOf(pnDto.getNrcStatus()));
                    } catch (Exception ignore) {
                    }
                    pn.setNrc(pnDto.getNrc());
                    pn.setNationalNrc(pnDto.getNationalNrc());
                    parentNrcRepository.save(pn);

                }
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The form was incomplete");
        }

        // Dorm
        if (dto.getDorm() != null) {
            EnrollmentDormDto d = dto.getDorm();
            EnrollmentDorm dorm = new EnrollmentDorm();
            dorm.setApplication(savedApplication);
            try {
                dorm.setDormFeeResponsible(EnrollmentDorm.DormFeeResponsible.valueOf(d.getDormFeeResponsible()));
            } catch (Exception ignore) {
            }
            dorm.setOtherName(d.getOtherName());
            dorm.setOtherRelationship(d.getOtherRelationship());
            dorm.setOtherAddress(d.getOtherAddress());
            dorm.setScholarshipSupport(d.getScholarshipSupport() != null ? d.getScholarshipSupport() : Boolean.FALSE);
            dorm.setScholarshipType(d.getScholarshipType());
            dorm.setAnotherScholarship(d.getAnotherScholarship());

            if (d.getOtherName() != null && !d.getOtherName().isBlank()) {
                dorm.setStatus(EnrollmentDorm.Status.PENDING);
                dormRepository.save(dorm);
            }
            dormRepository.save(dorm);
        }

        // ---- Files handling: use 'files' param (JSON file entries) first, fall back
        // to dto.getFiles() ----
        // FIXED: prefer files parameter (EnrollmentRequestFresher.FileEntry), fallback
        // to dto.getFiles() for compatibility
        boolean hasRequestFiles = files != null && !files.isEmpty();
        boolean hasDtoFiles = dto.getFiles() != null && !dto.getFiles().isEmpty();

        if (!hasRequestFiles && !hasDtoFiles) {
            // FIXED: require files from either request or dto
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The form was incomplete: files required");
        }

        // If request JSON file entries exist, process them

        if (hasRequestFiles) {
            for (EnrollmentRequestFresher.FileEntry entry : files) {
                if (entry == null || entry.getImage() == null || entry.getImage().isBlank())
                    continue;

                // Strip data URL prefix if present
                String fileData = entry.getImage();
                String base64Part;
                String mimeType = null;
                if (fileData.startsWith("data:")) {
                    int comma = fileData.indexOf(',');
                    if (comma < 0) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file data format");
                    }
                    String meta = fileData.substring(5, comma);
                    if (meta.contains(";")) {
                        mimeType = meta.substring(0, meta.indexOf(';'));
                    }
                    base64Part = fileData.substring(comma + 1);
                } else {
                    base64Part = fileData;
                }

                // Decode and size check
                final int MAX_BYTES = 10 * 1024 * 1024; // 10MB
                byte[] decoded;
                try {
                    decoded = Base64.getDecoder().decode(base64Part);
                } catch (IllegalArgumentException ex) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid base64 file data");
                }
                if (decoded.length > MAX_BYTES) {
                    throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File too large");
                }

                // Detect mime if missing
                if (mimeType == null || mimeType.isEmpty()) {
                    mimeType = guessImageMime(decoded); // your helper
                }

                if (mimeType == null || !mimeType.toLowerCase().startsWith("image/")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only image files are allowed");
                }

                // Map mime -> extension
                String ext = extensionForMime(mimeType);

                // Safe filename and write
                String safeFilename = UUID.randomUUID().toString() + (ext != null ? ext : "");
                Path target = uploadBase.resolve(safeFilename);
                try {
                    Files.write(target, decoded);
                } catch (Exception ex) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save file");
                }

                // Persist EnrollmentFile and cleanup on DB failure
                EnrollmentFile fileEntity = new EnrollmentFile();
                fileEntity.setApplication(savedApplication);

                // FIXED: map FileEntry.fileTypeId -> FileType id when numeric; otherwise leave
                // null (you can add name->id mapping here)
                if (entry.getFileTypeId() != null) {
                    try {
                        // attempt numeric id
                        Integer ftId = Integer.valueOf(entry.getFileTypeId().toString());
                        FileType ft = new FileType();
                        ft.setId(ftId);
                        fileEntity.setFileType(ft);
                    } catch (NumberFormatException ignore) {
                        // If your fileTypeId is a string code, map it to numeric id here.
                        // Example: if ("nrc_front") -> id 4 ; implement mapping logic as needed.
                    }
                }

                fileEntity.setFilePath("/uploads/" + safeFilename);
                fileEntity.setUploadedAt(LocalDateTime.now());

                try {
                    fileRepository.save(fileEntity);
                } catch (Exception ex) {
                    // print the *real* cause to logs
                    log.error("Failed to save fileEntity: {}", fileEntity, ex);

                    try {
                        Files.deleteIfExists(target);
                    } catch (Exception e) {
                        log.warn("Cleanup failed", e);
                    }

                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Failed to persist file metadata",
                            ex // keep original cause
                    );
                }

            }
        } else {
            // no request files, process dto.getFiles() (existing flow)
            for (EnrollmentFileDto fDto : dto.getFiles()) {
                String fileData = fDto.getFileData();
                if (fileData == null || fileData.isBlank())
                    continue;

                String base64Part;
                String mimeType = null;
                if (fileData.startsWith("data:")) {
                    int comma = fileData.indexOf(',');
                    if (comma < 0) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file data format");
                    }
                    String meta = fileData.substring(5, comma);
                    if (meta.contains(";")) {
                        mimeType = meta.substring(0, meta.indexOf(';'));
                    }
                    base64Part = fileData.substring(comma + 1);
                } else {
                    base64Part = fileData;
                }

                final int MAX_BYTES = 10 * 1024 * 1024;
                byte[] decoded;
                try {
                    decoded = Base64.getDecoder().decode(base64Part);
                } catch (IllegalArgumentException ex) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid base64 file data");
                }
                if (decoded.length > MAX_BYTES) {
                    throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File too large");
                }

                if (mimeType == null || mimeType.isEmpty()) {
                    mimeType = guessImageMime(decoded);
                }

                if (mimeType == null || !mimeType.toLowerCase().startsWith("image/")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only image files are allowed");
                }

                String ext = extensionForMime(mimeType);
                String safeFilename = UUID.randomUUID().toString() + (ext != null ? ext : "");
                Path target = uploadBase.resolve(safeFilename);
                try {
                    Files.write(target, decoded);
                } catch (Exception ex) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save file");
                }

                EnrollmentFile fileEntity = new EnrollmentFile();
                fileEntity.setApplication(savedApplication);
                if (fDto.getFileTypeId() != null) {
                    FileType ft = new FileType();
                    ft.setId(fDto.getFileTypeId());
                    fileEntity.setFileType(ft);
                }
                fileEntity.setFilePath("/uploads/" + safeFilename);
                fileEntity.setUploadedAt(fDto.getUploadedAt() != null ? fDto.getUploadedAt() : LocalDateTime.now());
                fileRepository.save(fileEntity);
            }
        }

        // Majors (preferences)
        if (dto.getMajors() != null) {
            List<EnrollmentMajor> toSave = new ArrayList<>();
            for (EnrollmentMajorDto mDto : dto.getMajors()) {
                EnrollmentMajor em = new EnrollmentMajor();
                em.setApplication(savedApplication);
                if (mDto.getMajorId() != null) {
                    Major mj = new Major();
                    mj.setId(mDto.getMajorId());
                    em.setMajor(mj);
                }
                em.setPreferenceRank(mDto.getPreferenceRank());
                toSave.add(em);
            }
            enrollmentMajorRepository.saveAll(toSave);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The form was incomplete");
        }

        // Finally mark the student as enrolled and save
        student.setEnrolled(true);
        studentRepository.save(student);
    }

    private String extensionForMime(String mime) {
        if (mime == null)
            return null;
        switch (mime.toLowerCase()) {
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            case "image/webp":
                return ".webp";
            case "image/bmp":
                return ".bmp";
            case "image/svg+xml":
                return ".svg";
            default:
                return ""; // unknown - store without extension or add mapping
        }
    }

    /**
     * Minimal magic-byte-based image detection for common types (fallback).
     * This is not exhaustive — for production prefer Apache Tika or file-type libs.
     */
    private String guessImageMime(byte[] b) {
        if (b.length >= 4) {
            // JPEG magic: FF D8 FF
            if ((b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF)
                return "image/jpeg";
            // PNG: 89 50 4E 47 0D 0A 1A 0A
            if ((b[0] & 0xFF) == 0x89 && (b[1] & 0xFF) == 0x50 && (b[2] & 0xFF) == 0x4E && (b[3] & 0xFF) == 0x47)
                return "image/png";
            // GIF: 47 49 46 38
            if ((b[0] & 0xFF) == 0x47 && (b[1] & 0xFF) == 0x49 && (b[2] & 0xFF) == 0x46 && (b[3] & 0xFF) == 0x38)
                return "image/gif";
            // WebP: "RIFF" + .... + "WEBP" - check first 12 bytes
            if (b.length >= 12 && (b[0] & 0xFF) == 0x52 && (b[1] & 0xFF) == 0x49 && (b[2] & 0xFF) == 0x46
                    && (b[3] & 0xFF) == 0x46
                    && (b[8] & 0xFF) == 0x57 && (b[9] & 0xFF) == 0x45 && (b[10] & 0xFF) == 0x42
                    && (b[11] & 0xFF) == 0x50) {
                return "image/webp";
            }
        }
        return null;
    }

    @Transactional
    public void updateField(Long enrollmentId, String path, String value, Long entityId) {
        EnrollmentApplication enrollment = applicationRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
        Student student = enrollment.getStudentId();

        switch (path) {
            case "academics.academicYear" -> {
                var academics = academicRepository.findByApplication(enrollment)
                        .orElseThrow(() -> new EntityNotFoundException("No academics found"));
                academics.setAcademicYear(value);
                academicRepository.save(academics);
            }

            case "academics.uniEntranceRollno" -> {
                var academics = academicRepository.findByApplication(enrollment)
                        .orElseThrow(() -> new EntityNotFoundException("No academics found"));
                academics.setUniEntranceRollno(value);
                academicRepository.save(academics);
            }

            case "student.rollNo" -> {
                student.setRollNo(value);
                studentRepository.save(student);
            }

            case "student.semester" -> {
                Long semesterId = Long.parseLong(value);
                MajorYear semester = majorYearRepository.findById(semesterId)
                        .orElseThrow(() -> new EntityNotFoundException("Semester not found"));
                student.setSemester(semester);
                studentRepository.save(student);
            }

            case "personal.birthDate" -> {
                var personal = personalRepository.findByApplication(enrollment)
                        .orElseThrow(() -> new EntityNotFoundException("No personal info found"));
                personal.setDateOfBirth(LocalDate.parse(value));
                personalRepository.save(personal);
            }

            // ================= Personal =================
            case "personal.fullNameMm" -> {
                var personal = personalRepository.findByApplication(enrollment)
                        .orElseThrow(() -> new EntityNotFoundException("No personal info found"));
                personal.setFullNameMm(value);
                personalRepository.save(personal);
            }

            case "personal.fullNameEn" -> {
                var personal = personalRepository.findByApplication(enrollment)
                        .orElseThrow(() -> new EntityNotFoundException("No personal info found"));
                personal.setFullNameEn(value);
                personalRepository.save(personal);
            }

            case "personal.gender" -> {
                var personal = personalRepository.findByApplication(enrollment)
                        .orElseThrow(() -> new EntityNotFoundException("No personal info found"));
                try {
                    ApplicantPersonal.Gender genderEnum = ApplicantPersonal.Gender.valueOf(value.toUpperCase());
                    personal.setGender(genderEnum);
                    personalRepository.save(personal);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid gender value: " + value);
                }
            }

            case "personal.dateOfBirth" -> {
                var personal = personalRepository.findByApplication(enrollment)
                        .orElseThrow(() -> new EntityNotFoundException("No personal info found"));
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate dob = LocalDate.parse(value, formatter);
                    personal.setDateOfBirth(dob);
                    personalRepository.save(personal);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid date format. Expected dd-MM-yyyy: " + value);
                }
            }

            case "personal.ethnic" -> {
                var personal = personalRepository.findByApplication(enrollment)
                        .orElseThrow(() -> new EntityNotFoundException("No personal info found"));
                personal.setEthnic(value);
                personalRepository.save(personal);
            }

            case "personal.religion" -> {
                var personal = personalRepository.findByApplication(enrollment)
                        .orElseThrow(() -> new EntityNotFoundException("No personal info found"));
                personal.setReligion(value);
                personalRepository.save(personal);
            }

            case "nrc.nrc" -> {
                var nrc = nrcRepository.findByApplication(enrollment)
                        .orElseThrow(() -> new EntityNotFoundException("No NRC found"));
                nrc.setNrc(value);
                nrcRepository.save(nrc);
            }

            case "academics.uniEntranceYear" -> {
                var academics = academicRepository.findByApplication(enrollment)
                        .orElseThrow(() -> new EntityNotFoundException("No academics found"));
                academics.setUniEntranceYear(value); // store directly as String
                academicRepository.save(academics);
            }

            case "student.major" -> {
                Long majorId;
                try {
                    majorId = Long.parseLong(value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid major ID format: " + value);
                }
                Major major = majorRepository.findById(majorId)
                        .orElseThrow(() -> new EntityNotFoundException("Major not found with id " + majorId));
                student.setMajor(major);
                studentRepository.save(student);
            }

            // ================= Contact =================
            case "contact.phoneNumber" -> {
                var contact = contactRepository.findByApplication(enrollment)
                        .orElseThrow(() -> new EntityNotFoundException("No contact info found"));
                contact.setPhoneNumber(value);
                contactRepository.save(contact);
            }

            case "contact.usualPhoneNumber" -> {
                var contact = contactRepository.findByApplication(enrollment)
                        .orElseThrow(() -> new EntityNotFoundException("No contact info found"));
                contact.setUsualPhoneNumber(value);
                contactRepository.save(contact);
            }

            case "contact.email" -> {
                var contact = contactRepository.findByApplication(enrollment)
                        .orElseThrow(() -> new EntityNotFoundException("No contact info found"));
                contact.setEmail(value);
                contactRepository.save(contact);
            }

            case "contact.birthPlace" -> {
                var contact = contactRepository.findByApplication(enrollment)
                        .orElseThrow(() -> new EntityNotFoundException("No contact info found"));
                contact.setBirthPlace(value);
                contactRepository.save(contact);
            }

            case "contact.fullAddress" -> {
                var contact = contactRepository.findByApplication(enrollment)
                        .orElseThrow(() -> new EntityNotFoundException("No contact info found"));
                contact.setFullAddress(value);
                contactRepository.save(contact);
            }

            // ================= Parents =================
            case "parents.parentType" -> {
                if (entityId == null)
                    throw new IllegalArgumentException("Parent ID is required");

                var parent = parentRepository.findById(entityId)
                        .orElseThrow(() -> new EntityNotFoundException("Parent not found with id " + entityId));

                try {
                    ApplicantParent.ParentType typeEnum = ApplicantParent.ParentType.valueOf(value.toUpperCase());
                    parent.setParentType(typeEnum);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid parent type: " + value);
                }

                parentRepository.save(parent);
            }

            case "parents.fullNameMm" -> {
                if (entityId == null)
                    throw new IllegalArgumentException("Parent ID is required");
                var parent = parentRepository.findById(entityId)
                        .orElseThrow(() -> new EntityNotFoundException("Parent not found with id " + entityId));
                parent.setFullNameMm(value);
                parentRepository.save(parent);
            }

            case "parents.fullNameEn" -> {
                if (entityId == null)
                    throw new IllegalArgumentException("Parent ID is required");
                var parent = parentRepository.findById(entityId)
                        .orElseThrow(() -> new EntityNotFoundException("Parent not found with id " + entityId));
                parent.setFullNameEn(value);
                parentRepository.save(parent);
            }

            case "parents.ethnic" -> {
                if (entityId == null)
                    throw new IllegalArgumentException("Parent ID is required");
                var parent = parentRepository.findById(entityId)
                        .orElseThrow(() -> new EntityNotFoundException("Parent not found with id " + entityId));
                parent.setEthnic(value);
                parentRepository.save(parent);
            }

            case "parents.religion" -> {
                if (entityId == null)
                    throw new IllegalArgumentException("Parent ID is required");
                var parent = parentRepository.findById(entityId)
                        .orElseThrow(() -> new EntityNotFoundException("Parent not found with id " + entityId));
                parent.setReligion(value);
                parentRepository.save(parent);
            }

            case "parents.occupation" -> {
                if (entityId == null)
                    throw new IllegalArgumentException("Parent ID is required");
                var parent = parentRepository.findById(entityId)
                        .orElseThrow(() -> new EntityNotFoundException("Parent not found with id " + entityId));
                parent.setOccupation(value);
                parentRepository.save(parent);
            }

            case "parents.isDeceased" -> {
                if (entityId == null)
                    throw new IllegalArgumentException("Parent ID is required");
                var parent = parentRepository.findById(entityId)
                        .orElseThrow(() -> new EntityNotFoundException("Parent not found with id " + entityId));
                parent.setIsDeceased(Boolean.parseBoolean(value));
                parentRepository.save(parent);
            }

            case "parents.dateOfDeath" -> {
                if (entityId == null)
                    throw new IllegalArgumentException("Parent ID is required");
                var parent = parentRepository.findById(entityId)
                        .orElseThrow(() -> new EntityNotFoundException("Parent not found with id " + entityId));
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    parent.setDateOfDeath(value);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid date format for dateOfDeath: " + value);
                }
                parentRepository.save(parent);
            }

            // ================= Parent NRC =================
            case "parents.nrcStatus" -> {
                if (entityId == null)
                    throw new IllegalArgumentException("Parent ID is required");

                var parent = parentRepository.findById(entityId)
                        .orElseThrow(() -> new EntityNotFoundException("Parent not found with id " + entityId));

                var nrc = parentNrcRepository.findByApplicantParent(parent)
                        .orElseThrow(() -> new EntityNotFoundException("Parent not found with id "));
                try {
                    ApplicantParentNrc.NrcStatus statusEnum = ApplicantParentNrc.NrcStatus.valueOf(value.toUpperCase());
                    nrc.setNrcStatus(statusEnum);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid NRC status value: " + value);
                }

                parentNrcRepository.save(nrc);
            }

            case "parents.nrc" -> {
                if (entityId == null)
                    throw new IllegalArgumentException("Parent ID is required");

                var parent = parentRepository.findById(entityId)
                        .orElseThrow(() -> new EntityNotFoundException("Parent not found with id " + entityId));

                var nrc = parentNrcRepository.findByApplicantParent(parent)
                        .orElseThrow(() -> new EntityNotFoundException("Parent not found with id "));

                nrc.setNrc(value);
                parentNrcRepository.save(nrc);
            }

            // ================= Parent Contact =================
            case "parents.phoneNumber" -> {
                if (entityId == null)
                    throw new IllegalArgumentException("Parent ID is required");

                var parent = parentRepository.findById(entityId)
                        .orElseThrow(() -> new EntityNotFoundException("Parent not found with id " + entityId));

                var contact = parentContactRepository.findByApplicantParent(parent)
                        .orElseThrow(() -> new EntityNotFoundException("Parent not found with id "));

                contact.setPhoneNumber(value);
                parentContactRepository.save(contact);
            }

            case "parents.fullAddress" -> {
                if (entityId == null)
                    throw new IllegalArgumentException("Parent ID is required");

                var parent = parentRepository.findById(entityId)
                        .orElseThrow(() -> new EntityNotFoundException("Parent not found with id " + entityId));

                var contact = parentContactRepository.findByApplicantParent(parent)
                        .orElseThrow(() -> new EntityNotFoundException("Parent not found with id "));

                contact.setFullAddress(value);
                parentContactRepository.save(contact);
            }

            default -> throw new IllegalArgumentException("Unsupported path: " + path);
        }
    }

}
