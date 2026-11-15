package edu.utycc.reg.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.utycc.reg.dto.StudentDashboardDto;
import edu.utycc.reg.entity.Student;
import edu.utycc.reg.repository.EnrollmentApplicationRepository;
import edu.utycc.reg.repository.StudentRepository;
import edu.utycc.reg.entity.EnrollmentApplication;
import edu.utycc.reg.entity.EnrollmentApplication.Status;

@Service
public class EnrollmentDashboardService {

    private final EnrollmentApplicationRepository enrollmentApplicationRepository;
    private final StudentRepository studentRepository;

    public EnrollmentDashboardService(EnrollmentApplicationRepository enrollmentApplicationRepository, StudentRepository studentRepository) {
        this.enrollmentApplicationRepository = enrollmentApplicationRepository;
        this.studentRepository = studentRepository;
    }

    public Page<StudentDashboardDto> getStudents(String filter, Pageable pageable) {
        Page<Student> apps;

        if (filter == null || filter.isBlank()) {
            apps = studentRepository.findAll(pageable);
        } else {
            // join with student name filter
            apps = studentRepository.findByNameContainingIgnoreCaseOrRollNoContainingIgnoreCase(filter,filter, pageable);
        }

        return apps.map(this::toDto);
    }

    public Page<StudentDashboardDto> getEnrollments(String filter, String status, Pageable pageable) {
        Page<EnrollmentApplication> apps;
        EnrollmentApplication.Status statusEnum = null;

        // Convert status string to enum safely
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = EnrollmentApplication.Status.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // invalid status, return empty page or handle error
                return Page.empty(pageable);
            }
        }

        boolean hasFilter = filter != null && !filter.isBlank();
        boolean hasStatus = statusEnum != null;

        if (hasFilter && hasStatus) {
            apps = enrollmentApplicationRepository
                .findByStudentId_NameContainingIgnoreCaseAndStatus(filter, statusEnum, pageable);
        } else if (hasFilter) {
            apps = enrollmentApplicationRepository
                .findByStudentId_NameContainingIgnoreCase(filter, pageable);
        } else if (hasStatus) {
            apps = enrollmentApplicationRepository
                .findByStatus(statusEnum, pageable);
        } else {
            apps = enrollmentApplicationRepository.findAll(pageable);
        }

        return apps.map(this::toDto);
    }


    private StudentDashboardDto toDto(EnrollmentApplication app) {
        Student student = app.getStudentId();

        StudentDashboardDto dto = new StudentDashboardDto();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setRollNo(student.getRollNo());
        dto.setMajor(student.getMajor().getName());
        dto.setSemester(student.getSemester().getYearLabel());
        dto.setEnrolled(student.isEnrolled());
        dto.setBirthDate(student.getBirthDate());

        // enrollment-specific info
        dto.setEnrollmentId(app.getId());
        dto.setEnrollmentStatus(app.getStatus());

        return dto;
    }

    private StudentDashboardDto toDto(Student student) {
        StudentDashboardDto dto = new StudentDashboardDto();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setRollNo(student.getRollNo());
        dto.setMajor(student.getMajor().getName());
        dto.setSemester(student.getSemester().getYearLabel());
        dto.setEnrolled(student.isEnrolled());
        dto.setBirthDate(student.getBirthDate());

        return dto;
    }

    public long countEnrollments(){
        return enrollmentApplicationRepository.countByStatus(Status.PENDING);
    }
}

