package edu.utycc.reg.controller;

import edu.utycc.reg.service.AnnouncementService;
import edu.utycc.reg.service.EnrollmentDashboardService;
import edu.utycc.reg.service.EnrollmentFileService;
import edu.utycc.reg.service.PreEnrollmentOldStudentService;
import edu.utycc.reg.service.PreEnrollmentService;
import edu.utycc.reg.service.UserService;
import edu.utycc.reg.dto.PreEnrollmentOldSummaryDTO;
import edu.utycc.reg.dto.PreEnrollmentSummaryDTO;
import edu.utycc.reg.dto.RecentUserDTO;
import edu.utycc.reg.dto.StudentDashboardDto;
import edu.utycc.reg.entity.Announcement;
import edu.utycc.reg.entity.PreEnrollment;
import edu.utycc.reg.entity.PreEnrollmentOldStudent;
import edu.utycc.reg.entity.User;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN') or hasAuthority('USER_READ')")
@RequestMapping("/private/admin")
public class DashboardController {

    private final UserService userService;
    private final PreEnrollmentService preEnrollmentService;
    private final EnrollmentFileService fileService;
    private final EnrollmentDashboardService enrollmentDashboardService;
    private final PreEnrollmentOldStudentService preEnrollmentOldStudentService;
    private final AnnouncementService annoucementService;

    public DashboardController(PreEnrollmentService preEnrollmentService,
                               EnrollmentFileService fileService,
                               UserService userService,
                               EnrollmentDashboardService enrollmentDashboardService,
                               PreEnrollmentOldStudentService preEnrollmentOldStudentService,
                               AnnouncementService annoucementService) {
        this.preEnrollmentService = preEnrollmentService;
        this.fileService = fileService;
        this.userService = userService;
        this.enrollmentDashboardService = enrollmentDashboardService;
        this.preEnrollmentOldStudentService = preEnrollmentOldStudentService;
        this.annoucementService = annoucementService;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("usersCount", userService.countUsers());
        stats.put("preEnrollments", preEnrollmentService.countPreEnrollments());
        stats.put("enrollments", enrollmentDashboardService.countEnrollments());
        stats.put("filesCount", fileService.countAllFiles());

        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double cpuLoad = osBean.getSystemCpuLoad();
        stats.put("load", cpuLoad >= 0 ? Math.round(cpuLoad * 100) : "—");

        return stats;
    }

    @GetMapping("/recent_users")
    public List<RecentUserDTO> getRecentUsers() {
        return userService.getRecentUsers(3);
    }

    @GetMapping("/pre_enrollments")
    public List<PreEnrollmentSummaryDTO> getRecentPreEnrollments() {
        return preEnrollmentService.getRecentPreEnrollments(3);
    }

    @GetMapping("/section_users")
    public Map<String, Object> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestParam(required = false) String filter
    ) {
        // split sort if front-end sends "id,asc" format
        String[] sortParts = sort.split(",");
        String sortProperty = sortParts[0];
        Sort.Direction direction = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (sortParts.length > 1) {
            direction = sortParts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));
        Page<User> usersPage = userService.getUsers(filter, pageable);

        return Map.of(
                "content", usersPage.getContent()
                    .stream()
                    .map(RecentUserDTO::fromEntity)
                    .collect(Collectors.toList()),
                "totalPages", usersPage.getTotalPages(),
                "totalElements", usersPage.getTotalElements()
        );
    }

    @GetMapping("/section_pre_enrollments")
    public Map<String, Object> getPreEnrollment(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String status
    ) {
        // split sort if front-end sends "id,asc" format
        String[] sortParts = sort.split(",");
        String sortProperty = sortParts[0];
        Sort.Direction direction = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (sortParts.length > 1) {
            direction = sortParts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));
        Page<PreEnrollment> preEnrollmentPage = preEnrollmentService.getPreEnrollments(filter, status, pageable);

        return Map.of(
                "content", preEnrollmentPage.getContent()
                    .stream()
                    .map(PreEnrollmentSummaryDTO::fromEntity)
                    .collect(Collectors.toList()),
                "totalPages", preEnrollmentPage.getTotalPages(),
                "totalElements", preEnrollmentPage.getTotalElements()
        );
    }

    @GetMapping("/section_pre_enrollments_old")
    public Map<String, Object> getPreEnrollmentOld(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String status
    ) {
        // split sort if front-end sends "id,asc" format
        String[] sortParts = sort.split(",");
        String sortProperty = sortParts[0];
        Sort.Direction direction = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (sortParts.length > 1) {
            direction = sortParts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));
        Page<PreEnrollmentOldStudent> preEnrollmentPage = preEnrollmentOldStudentService.getPreEnrollments(filter, status, pageable);

        return Map.of(
                "content", preEnrollmentPage.getContent()
                    .stream()
                    .map(PreEnrollmentOldSummaryDTO::fromEntity)
                    .collect(Collectors.toList()),
                "totalPages", preEnrollmentPage.getTotalPages(),
                "totalElements", preEnrollmentPage.getTotalElements()
        );
    }

    @GetMapping("/section_enrollments")
    public Map<String, Object> getEnrollment(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String status) 
    {
        String[] sortParts = sort.split(",");
        String sortProperty = sortParts[0];
        Sort.Direction direction = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (sortParts.length > 1) {
            direction = sortParts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));
        Page<StudentDashboardDto> enrollmentPage = enrollmentDashboardService.getEnrollments(filter, status, pageable);

        return Map.of(
                "content", enrollmentPage.getContent(),
                "totalPages", enrollmentPage.getTotalPages(),
                "totalElements", enrollmentPage.getTotalElements()
        );
    }

    @GetMapping("/section_students")
    public Map<String, Object> getStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestParam(required = false) String filter) 
    {
        String[] sortParts = sort.split(",");
        String sortProperty = sortParts[0];
        Sort.Direction direction = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (sortParts.length > 1) {
            direction = sortParts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));
        Page<StudentDashboardDto> enrollmentPage = enrollmentDashboardService.getStudents(filter, pageable);

        return Map.of(
                "content", enrollmentPage.getContent(),
                "totalPages", enrollmentPage.getTotalPages(),
                "totalElements", enrollmentPage.getTotalElements()
        );
    }

    @GetMapping("/section_annoucements")
    public Map<String, Object> getAnnoucements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestParam(required = false) String filter) 
    {
        String[] sortParts = sort.split(",");
        String sortProperty = sortParts[0];
        Sort.Direction direction = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (sortParts.length > 1) {
            direction = sortParts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));
        Page<Announcement> enrollmentPage = annoucementService.getAnnoucements(filter, pageable);

        return Map.of(
                "content", enrollmentPage.getContent(),
                "totalPages", enrollmentPage.getTotalPages(),
                "totalElements", enrollmentPage.getTotalElements()
        );
    }
}
