package edu.utycc.reg.service;

import edu.utycc.reg.entity.*;
import edu.utycc.reg.repository.*;
import edu.utycc.reg.service.AdminEnrollmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import edu.utycc.reg.entity.EnrollmentApplication.Status;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class AdminEnrollmentService {

    private final EnrollmentApplicationRepository applicationRepository;
    private final ApplicationApprovalRepository approvalRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final ApplicantPersonalRepository personalApp;

    public AdminEnrollmentService (EnrollmentApplicationRepository applicationRepository,
                                      ApplicationApprovalRepository approvalRepository,
                                      StudentRepository studentRepository,
                                      ApplicantPersonalRepository personalApp,
                                      UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.approvalRepository = approvalRepository;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.personalApp = personalApp;
    }

    private ApplicationApproval.ApproverRole mapUserToApproverRole(User user) {
        if (user == null) return ApplicationApproval.ApproverRole.admin; // default

        Set<Role> roles = user.getRoles();
        if (roles == null || roles.isEmpty()) return ApplicationApproval.ApproverRole.admin;

        for (Role r : roles) {
            if (r == null || r.getName() == null) continue;
            String rn = r.getName().trim().toLowerCase();

            if (rn.contains("admin")) return ApplicationApproval.ApproverRole.admin;
            if (rn.contains("hod")) return ApplicationApproval.ApproverRole.hod;
            if (rn.contains("finance")) return ApplicationApproval.ApproverRole.finance;
            if (rn.contains("warden") || rn.contains("dorm")) return ApplicationApproval.ApproverRole.dorm_warden;
            if (rn.contains("student") || rn.contains("affair")) return ApplicationApproval.ApproverRole.student_affair;
            // try underscore variants
            if (rn.equals("student_affair")) return ApplicationApproval.ApproverRole.student_affair;
            if (rn.equals("dorm_warden")) return ApplicationApproval.ApproverRole.dorm_warden;
        }

        // fallback to first role name mapping attempt (if any)
        Role first = roles.iterator().next();
        if (first != null && first.getName() != null && first.getName().toLowerCase().contains("admin")) {
            return ApplicationApproval.ApproverRole.admin;
        }

        return ApplicationApproval.ApproverRole.admin;
    }


    private <E extends Enum<E>> E resolveEnum(Class<E> enumClass, String... candidates) {
        for (String c : candidates) {
            if (c == null) continue;
            try {
                return Enum.valueOf(enumClass, c);
            } catch (IllegalArgumentException ignored) {}
        }
        // try uppercase/lowercase variants
        for (String c : candidates) {
            if (c == null) continue;
            try {
                return Enum.valueOf(enumClass, c.toUpperCase());
            } catch (IllegalArgumentException ignored) {}
            try {
                return Enum.valueOf(enumClass, c.toLowerCase());
            } catch (IllegalArgumentException ignored) {}
        }
        throw new IllegalArgumentException("No matching enum constant for " + enumClass.getSimpleName());
    }
    
    @Transactional
    public void acceptEnrollment(Long enrollmentId, String approverUsername) {
        EnrollmentApplication app = applicationRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment application not found"));

        Status currentStatus = app.getStatus();
        if (currentStatus == Status.ACCEPTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Application already accepted");
        }else if (currentStatus == Status.REJECTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Application already rejected");
        }

        // Try to set EnrollmentApplication.Status to an ACCEPTED/APPROVED like value.
        try {
            // common candidates: ACCEPTED, APPROVED (adjust if your enum uses different names)
            EnrollmentApplication.Status newStatus = resolveEnum(EnrollmentApplication.Status.class, "ACCEPTED", "APPROVED", "accepted", "approved");
            app.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not resolve EnrollmentApplication.Status for accepted state. Update enums or adapt service.");
        }

        applicationRepository.save(app);

        // Build approval record
        ApplicationApproval approval = new ApplicationApproval();
        approval.setApplication(app);

        // find approver user if possible
        Optional<User> approverUserOpt = userRepository.findByEmail(approverUsername);
        User approverUser = approverUserOpt.orElse(null);

        // map user roles -> approver role (uses exact enum identifiers defined in your ApplicationApproval)
        ApplicationApproval.ApproverRole approverRole = mapUserToApproverRole(approverUser);
        approval.setApproverRole(approverRole);

        // set approval status to 'approved' (matches ApplicationApproval.ApprovalStatus enum)
        approval.setStatus(ApplicationApproval.ApprovalStatus.approved);

        approval.setApprovedAt(LocalDateTime.now());
        approval.setRemarks(null);
        approvalRepository.save(approval);

        // mark student enrolled true
        Student student = app.getStudentId();
        if (student != null) {
            EnrollmentApplication application = applicationRepository.findById(enrollmentId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment not found"));

            ApplicantPersonal dobApp = personalApp.findByApplication(application)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment not found"));

            student.setBirthDate(dobApp.getDateOfBirth());
            student.setEnrolled(true);
            studentRepository.save(student);
        }
    }

    @Transactional
    public void rejectEnrollment(Long enrollmentId, String remark, String approverUsername) {
        EnrollmentApplication app = applicationRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment application not found"));

        Status currentStatus = app.getStatus();
        if (currentStatus == Status.ACCEPTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Application already accepted");
        }else if (currentStatus == Status.REJECTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Application already rejected");
        }

        // set application status -> try REJECTED / DENIED variants
        try {
            EnrollmentApplication.Status newStatus = resolveEnum(EnrollmentApplication.Status.class, "REJECTED", "DENIED", "rejected", "denied");
            app.setStatus(newStatus);
            app.setRemark(remark);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not resolve EnrollmentApplication.Status for rejected state. Update enums or adapt service.");
        }

        applicationRepository.save(app);

        // create approval record with remark
        ApplicationApproval approval = new ApplicationApproval();
        approval.setApplication(app);

        Optional<User> approverUserOpt = userRepository.findByEmail(approverUsername);
        User approverUser = approverUserOpt.orElse(null);

        ApplicationApproval.ApproverRole approverRole = mapUserToApproverRole(approverUser);
        approval.setApproverRole(approverRole);

        // set approval status to 'rejected' (matches ApplicationApproval.ApprovalStatus enum)
        approval.setStatus(ApplicationApproval.ApprovalStatus.rejected);

        // mark student enrolled false
        Student student = app.getStudentId();
        if (student != null) {
            student.setEnrolled(false);
            studentRepository.save(student);
        }

        approval.setApprovedAt(LocalDateTime.now());
        approval.setRemarks(remark);
        approvalRepository.save(approval);
    }
}
