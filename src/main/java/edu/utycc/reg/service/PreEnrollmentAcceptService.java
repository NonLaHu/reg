package edu.utycc.reg.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.utycc.reg.dto.AcceptResponsePreEnroll;
import edu.utycc.reg.entity.Major;
import edu.utycc.reg.entity.MajorYear;
import edu.utycc.reg.entity.OldStudentTrack;
import edu.utycc.reg.entity.PreEnrollment;
import edu.utycc.reg.entity.PreEnrollmentOldStudent;
import edu.utycc.reg.entity.Role;
import edu.utycc.reg.entity.Student;
import edu.utycc.reg.entity.User;
import edu.utycc.reg.repository.MajorRepository;
import edu.utycc.reg.repository.MajorYearRepository;
import edu.utycc.reg.repository.OldStudentTrackRepository;
import edu.utycc.reg.repository.PreEnrollmentOldStudentRepository;
import edu.utycc.reg.repository.PreEnrollmentRepository;
import edu.utycc.reg.repository.RoleRepository;
import edu.utycc.reg.repository.StudentRepository;
import edu.utycc.reg.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

@Service
public class PreEnrollmentAcceptService {

    private final PreEnrollmentRepository preRepo;
    private final PreEnrollmentOldStudentRepository preOldRepo;
    private final UserRepository userRepo;
    private final StudentRepository studentRepo;
    private final MajorRepository majorRepo;
    private final MajorYearRepository majorYearRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepo;
    private final OldStudentTrackRepository oldStudentTrackRepo;

    private final SecureRandom secureRandom = new SecureRandom();
    private static final String EMAIL_SUFFIX = "@utycc.student";
    private static final String EMAIL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String PASSWORD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public PreEnrollmentAcceptService(PreEnrollmentRepository preRepo,
                                      RoleRepository roleRepo,
                                      UserRepository userRepo,
                                      StudentRepository studentRepo,
                                      MajorRepository majorRepo,
                                      MajorYearRepository majorYearRepo,
                                      BCryptPasswordEncoder passwordEncoder,
                                      PreEnrollmentOldStudentRepository preOldRepo,
                                      OldStudentTrackRepository oldStudentTrackRepo) {
        this.preRepo = preRepo;
        this.userRepo = userRepo;
        this.studentRepo = studentRepo;
        this.majorRepo = majorRepo;
        this.majorYearRepo = majorYearRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.preOldRepo = preOldRepo;
        this.oldStudentTrackRepo = oldStudentTrackRepo;
    }

    @Transactional
    public AcceptResponsePreEnroll acceptPreEnrollment(Long preEnrollmentId) {
        PreEnrollment pre = preRepo.findById(preEnrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("PreEnrollment not found: " + preEnrollmentId));

        if (pre.getStatus() == PreEnrollment.Status.ACCEPTED) {
            return new AcceptResponsePreEnroll("ALREADY_ACCEPTED", "PreEnrollment already accepted");
        }else if (pre.getStatus() == PreEnrollment.Status.REJECTED) {
            return new AcceptResponsePreEnroll("ALREADY_REJECTED", "PreEnrollment already rejected");
        }

            // Generate serial email and password with retries for uniqueness
        String serialEmail = null;
        String rawPassword = null;

        int attempts = 0;
        final int maxAttempts = 50;
        while (attempts < maxAttempts) {
            attempts++;
            serialEmail = generateSerialEmail();
            // quick uniqueness check
            if (userRepo.findByEmail(serialEmail).isPresent()) {
                serialEmail = null;
                continue;
            }

            rawPassword = generateRandomString(10, PASSWORD_CHARS);
            String hashed = passwordEncoder.encode(rawPassword);

            // create user and student in a safe block - catch unique constraint race
            try {
                User user = new User();
                user.setEmail(serialEmail);
                user.setPasswordHash(hashed);
                user.setFullName(pre.getFullNameMm());
                user.setUserType(User.UserType.STUDENT);
                user.setStatus(User.Status.ACTIVE);

                // Assign STUDENT role to the user
                Role studentRole = roleRepo.findById(6L)
                        .orElseThrow(() -> new IllegalStateException("Role STUDENT not found"));
                user.getRoles().add(studentRole);

                user = userRepo.save(user);

                // Create student
                Student student = new Student();
                student.setUser(user);
                student.setPreEnrollment(pre);

                student.setName(pre.getFullNameMm() != null ? pre.getFullNameMm() : "Unknown");
                student.setRollNo("N/A");

                Major defaultMajor = majorRepo.findById(0L)
                        .orElseThrow(() -> new IllegalStateException("Default Major with ID 0 not found"));

                MajorYear defaultYear = majorYearRepo.findById(0L)
                        .orElseThrow(() -> new IllegalStateException("Default MajorYear with ID 0 not found"));

                student.setMajor(defaultMajor);
                student.setSemester(defaultYear);


                // birth date may be absent; leave null if not set
                student.setBirthDate(pre.getBirthOfDate());

                student.setEnrolled(false);

                student = studentRepo.save(student);

                // Update pre-enrollment status
                pre.setStatus(PreEnrollment.Status.ACCEPTED);
                preRepo.save(pre);

                // success -> break loop and return
                break;

            } catch (DataIntegrityViolationException e) {
                // possible unique constraint violation on email; try again
                serialEmail = null;
                rawPassword = null;
                // continue loop to retry
            }
        }

        if (serialEmail == null || rawPassword == null) {
            throw new IllegalStateException("Failed to create user after multiple attempts");
        }

        return new AcceptResponsePreEnroll(serialEmail, rawPassword);
    }


    @Transactional
    public AcceptResponsePreEnroll acceptPreEnrollmentOld(Long preEnrollmentOldId) {
        PreEnrollmentOldStudent pre = preOldRepo.findById(preEnrollmentOldId)
                .orElseThrow(() -> new IllegalArgumentException("PreEnrollment not found: " + preEnrollmentOldId));

        if (pre.getStatus() == PreEnrollmentOldStudent.Status.ACCEPTED) {
            return new AcceptResponsePreEnroll("ALREADY_ACCEPTED", "PreEnrollment already accepted");
        }else if (pre.getStatus() == PreEnrollmentOldStudent.Status.REJECTED) {
            return new AcceptResponsePreEnroll("ALREADY_REJECTED", "PreEnrollment already rejected");
        }

            // Generate serial email and password with retries for uniqueness
        String serialEmail = null;
        String rawPassword = null;

        int attempts = 0;
        final int maxAttempts = 50;
        while (attempts < maxAttempts) {
            attempts++;
            serialEmail = generateSerialEmail();
            // quick uniqueness check
            if (userRepo.findByEmail(serialEmail).isPresent()) {
                serialEmail = null;
                continue;
            }

            rawPassword = generateRandomString(10, PASSWORD_CHARS);
            String hashed = passwordEncoder.encode(rawPassword);

            // create user and student in a safe block - catch unique constraint race
            try {
                User user = new User();
                user.setEmail(serialEmail);
                user.setPasswordHash(hashed);
                user.setFullName(pre.getFullNameMm());
                user.setUserType(User.UserType.STUDENT);
                user.setStatus(User.Status.ACTIVE);

                // Assign STUDENT role to the user
                Role studentRole = roleRepo.findById(6L)
                        .orElseThrow(() -> new IllegalStateException("Role STUDENT not found"));
                user.getRoles().add(studentRole);

                user = userRepo.save(user);

                // Create student
                Student student = new Student();
                student.setUser(user);
                student.setPreEnrollment(preRepo.getById(0L));

                student.setName(pre.getFullNameMm() != null ? pre.getFullNameMm() : "Unknown");
                student.setRollNo(pre.getPrevRollNo());

                student.setMajor(pre.getMajor());
                student.setSemester(pre.getMajorYear());


                // birth date may be absent; leave null if not set
                student.setBirthDate(null);

                student.setEnrolled(false);

                student = studentRepo.save(student);

                // Update pre-enrollment status
                pre.setStatus(PreEnrollmentOldStudent.Status.ACCEPTED);
                preOldRepo.save(pre);

                OldStudentTrack link = new OldStudentTrack()
                        .setOldStudent(student)
                        .setPreEnrollment(pre);
                oldStudentTrackRepo.save(link);

                // success -> break loop and return
                break;

            } catch (DataIntegrityViolationException e) {
                // possible unique constraint violation on email; try again
                serialEmail = null;
                rawPassword = null;
                // continue loop to retry
            }
        }

        if (serialEmail == null || rawPassword == null) {
            throw new IllegalStateException("Failed to create user after multiple attempts");
        }

        return new AcceptResponsePreEnroll(serialEmail, rawPassword);
    }

    @Transactional
    public AcceptResponsePreEnroll rejectPreEnrollmentOld(Long id, String reason) {
        PreEnrollmentOldStudent pre = preOldRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PreEnrollment not found: " + id));

        if (pre.getStatus() == PreEnrollmentOldStudent.Status.ACCEPTED) {
            return new AcceptResponsePreEnroll("ALREADY_ACCEPTED", "PreEnrollment already accepted");
        }else if (pre.getStatus() == PreEnrollmentOldStudent.Status.REJECTED) {
            return new AcceptResponsePreEnroll("ALREADY_REJECTED", "PreEnrollment already rejected");
        }

        pre.setStatus(PreEnrollmentOldStudent.Status.REJECTED);
        pre.setRemarks(reason);
        preOldRepo.save(pre);

        return new AcceptResponsePreEnroll("REJECTED", "PreEnrollment has been rejected successfully");
    }

    @Transactional
    public AcceptResponsePreEnroll rejectPreEnrollment(Long id, String reason) {
        PreEnrollment preEnrollment = preRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PreEnrollment not found with id: " + id));

        if (preEnrollment.getStatus() == PreEnrollment.Status.ACCEPTED) {
            return new AcceptResponsePreEnroll("ALREADY_ACCEPTED", "PreEnrollment already accepted");
        } else if (preEnrollment.getStatus() == PreEnrollment.Status.REJECTED) {
            return new AcceptResponsePreEnroll("ALREADY_REJECTED", "PreEnrollment already rejected");
        }

        preEnrollment.setStatus(PreEnrollment.Status.REJECTED);
        preEnrollment.setRemarks(reason);
        preRepo.save(preEnrollment);

        return new AcceptResponsePreEnroll("REJECTED", "PreEnrollment has been rejected successfully");
    }

    private String generateSerialEmail() {
        String key = generateRandomString(5, EMAIL_CHARS);
        return key + EMAIL_SUFFIX;
    }

    private String generateRandomString(int length, String alphabet) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = secureRandom.nextInt(alphabet.length());
            sb.append(alphabet.charAt(idx));
        }
        return sb.toString();
    }
}