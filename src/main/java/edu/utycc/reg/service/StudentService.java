package edu.utycc.reg.service;

import edu.utycc.reg.dto.*;
import edu.utycc.reg.entity.*;
import edu.utycc.reg.exception.ResourceNotFoundException;
import edu.utycc.reg.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final EnrollmentApplicationRepository enrollmentApplicationRepository;
    private final OldStudentTrackRepository oldstudentTrackRepo;
    private final EnrollmentDormRepository enrollmentDormRepository;

    // optional base url to expose file assets used for passCertificate
    private final String fileBaseUrl;

    public StudentService(
            StudentRepository studentRepository,
            EnrollmentApplicationRepository enrollmentApplicationRepository,
            OldStudentTrackRepository oldstudentTrackRepo,
            EnrollmentDormRepository enrollmentDormRepository,
            @Value("${app.fileBaseUrl:}") String fileBaseUrl) {
        this.studentRepository = studentRepository;
        this.oldstudentTrackRepo = oldstudentTrackRepo;
        this.enrollmentApplicationRepository = enrollmentApplicationRepository;
        this.fileBaseUrl = fileBaseUrl == null ? "" : fileBaseUrl;
        this.enrollmentDormRepository = enrollmentDormRepository;
    }

    @Transactional(readOnly = true)
    public StudentDetailDto getById(Long id) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        return mapToDto(s);
    }

    @Transactional(readOnly = true)
    public StudentDetailDto getByUserId(Long id) {
        return studentRepository.findByUserId(id)
                .map(this::mapToDto)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public StudentDetailDto getByRollNo(String rollNo) {
        Student s = studentRepository.findByRollNo(rollNo)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "rollNo", rollNo));
        return mapToDto(s);
    }

    private StudentDetailDto mapToDto(Student s) {
        StudentDetailDto dto = new StudentDetailDto();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setRollNo(s.getRollNo());
        dto.setEnrolled(s.isEnrolled());
        dto.setBirthDate(s.getBirthDate());
        dto.setCreatedAt(s.getCreatedAt());

        // compute age safely
        dto.setAge(computeAge(s.getBirthDate()));

        OldStudentTrack oldTrack = oldstudentTrackRepo.findByOldStudent_Id(s.getId());

        if (oldTrack != null) {
            PreEnrollmentOldStudent pre = oldTrack.getPreEnrollment();

            PreEnrollmentOldDTO dtoOld = new PreEnrollmentOldDTO();
            dtoOld.setId(pre.getId());
            dtoOld.setGender(pre.getGender());
            dtoOld.setFullNameMm(pre.getFullNameMm());
            dtoOld.setPrevRollNo(pre.getPrevRollNo());
            dtoOld.setEmail(pre.getEmail());
            dtoOld.setNrc(pre.getNrc());
            dtoOld.setPhoneNumber(pre.getPhoneNumber());
            dtoOld.setViberPhoneNumber(pre.getViberPhoneNumber());
            dtoOld.setRemarks(pre.getRemarks());
            dtoOld.setMajor(pre.getMajor().getCode());
            dtoOld.setMajorYear(pre.getMajorYear().getYearLabel());
            dtoOld.setStatus(pre.getStatus());
            dtoOld.setInformed(pre.getInformed());
            dtoOld.setCreatedAt(pre.getCreatedAt());
            dtoOld.setStudentIdCardUrl(pre.getStudentIdCardUrl());

            dto.setPreEnrollmentOldDTO(dtoOld);
        } else {
            PreEnrollment pe = s.getPreEnrollment();
            if (pe != null) {
                PreEnrollmentDto ped = new PreEnrollmentDto();
                ped.setId(pe.getId());
                ped.setGender(pe.getGender() == null ? null : pe.getGender().name());
                ped.setEmail(pe.getEmail());
                ped.setFullNameMm(pe.getFullNameMm());
                ped.setUniEntranceRollno(pe.getUniEntranceRollno());
                ped.setUniEntranceAcademicYear(pe.getUniEntranceAcademicYear());
                ped.setNrc(pe.getNrc());
                ped.setPassCertificateUrl(pe.getPassCertificate());
                ped.setStatus(pe.getStatus() == null ? null : pe.getStatus().name());
                ped.setInformed(pe.getInformed());
                ped.setPhoneNumber(pe.getPhoneNumber());
                ped.setViberPhoneNumber(pe.getViberPhoneNumber());
                ped.setCreatedAt(pe.getCreatedAt());
                if (pe.getPassCertificate() != null && !fileBaseUrl.isBlank()) {
                    ped.setPassCertificateUrl(fileBaseUrl + "/" + pe.getPassCertificate());
                }
                dto.setPreEnrollment(ped);
            }
        }

        // user (optional fields)
        User u = s.getUser();
        if (u != null) {
            UserSummaryDto ud = new UserSummaryDto();
            ud.setId(u.getId());
            ud.setFullName(u.getFullName());
            ud.setEmail(u.getEmail());
            dto.setUser(ud);
        }

        // major
        Major m = s.getMajor();
        if (m != null) {
            MajorDto md = new MajorDto();
            md.setId(m.getId());
            md.setCode(m.getCode());
            md.setName(m.getName());
            dto.setMajor(md);
        }

        // semester / majorYear
        MajorYear my = s.getSemester();
        if (my != null) {
            MajorYearDto myd = new MajorYearDto();
            myd.setId(my.getId());
            myd.setYearLabel(my.getYearLabel());
            myd.setYearOrder(my.getYearOrder());
            dto.setSemester(myd);
        }

        // enrollment applications: fetched separately because Student entity does not
        // contain mapped list
        List<EnrollmentApplication> apps = enrollmentApplicationRepository.findByStudentIdOrderByCreatedAtDesc(s);
        List<EnrollmentApplicationDto> appDtos = apps.stream().map(app -> {
            EnrollmentApplicationDto a = new EnrollmentApplicationDto();
            a.setId(app.getId());
            a.setStatus(app.getStatus() == null ? null : app.getStatus());
            a.setRemark(app.getRemark() == null ? null : app.getRemark());
            a.setIsFresher(app.getIsFresher());
            a.setCreatedAt(app.getCreatedAt());
            a.setUpdatedAt(app.getUpdatedAt());
            Optional<EnrollmentDorm> dormOpt = enrollmentDormRepository.findByApplicationId(app.getId());
            a.setDormStatus(dormOpt.map(EnrollmentDorm::getStatus).orElse(null));
            return a;
        }).collect(Collectors.toList());
        dto.setEnrollmentApplications(appDtos);

        return dto;
    }

    private Integer computeAge(LocalDate birthDate) {
        if (birthDate == null)
            return null;
        LocalDate now = LocalDate.now();
        if (birthDate.isAfter(now))
            return null;
        return Period.between(birthDate, now).getYears();
    }
}
