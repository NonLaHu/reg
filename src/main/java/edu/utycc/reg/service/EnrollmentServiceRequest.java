package edu.utycc.reg.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import edu.utycc.reg.dto.ApplicantAcademicDto;
import edu.utycc.reg.dto.ApplicantContactDto;
import edu.utycc.reg.dto.ApplicantNrcDto;
import edu.utycc.reg.dto.ApplicantParentContactDto;
import edu.utycc.reg.dto.ApplicantParentDto;
import edu.utycc.reg.dto.ApplicantParentNrcDto;
import edu.utycc.reg.dto.ApplicantPersonalDto;
import edu.utycc.reg.dto.ApplicationApprovalDto;
import edu.utycc.reg.dto.EnrollmentApplicationDto;
import edu.utycc.reg.dto.EnrollmentDormDto;
import edu.utycc.reg.dto.EnrollmentFileDto;
import edu.utycc.reg.dto.EnrollmentMajorDto;
import edu.utycc.reg.dto.StudentDashboardDto;
import edu.utycc.reg.entity.EnrollmentApplication;
import edu.utycc.reg.entity.Student;
import edu.utycc.reg.repository.ApplicantAcademicRepository;
import edu.utycc.reg.repository.ApplicantContactRepository;
import edu.utycc.reg.repository.ApplicantNrcRepository;
import edu.utycc.reg.repository.ApplicantParentContactRepository;
import edu.utycc.reg.repository.ApplicantParentNrcRepository;
import edu.utycc.reg.repository.ApplicantParentRepository;
import edu.utycc.reg.repository.ApplicantPersonalRepository;
import edu.utycc.reg.repository.ApplicationApprovalRepository;
import edu.utycc.reg.repository.EnrollmentApplicationRepository;
import edu.utycc.reg.repository.EnrollmentDormRepository;
import edu.utycc.reg.repository.EnrollmentFileRepository;
import edu.utycc.reg.repository.EnrollmentMajorRepository;
import edu.utycc.reg.repository.FileTypeRepository;

@Service
public class EnrollmentServiceRequest {
    private final EnrollmentApplicationRepository applicationRepository;
    private final ApplicantPersonalRepository personalRepository;
    private final ApplicantContactRepository contactRepository;
    private final ApplicantNrcRepository nrcRepository;
    private final ApplicantAcademicRepository academicRepository;
    private final ApplicantParentRepository parentRepository;
    private final ApplicantParentContactRepository parentContactRepository;
    private final ApplicantParentNrcRepository parentNrcRepository;
    private final EnrollmentDormRepository dormRepository;
    private final EnrollmentFileRepository fileRepository;
    private final EnrollmentMajorRepository majorRepository;
    private final ApplicationApprovalRepository approvalRepository;
    private final FileTypeRepository fileTypeRepo;

    public EnrollmentServiceRequest(
            EnrollmentApplicationRepository applicationRepository,
            ApplicantPersonalRepository personalRepository,
            ApplicantContactRepository contactRepository,
            ApplicantNrcRepository nrcRepository,
            ApplicantAcademicRepository academicRepository,
            ApplicantParentRepository parentRepository,
            ApplicantParentContactRepository parentContactRepository,
            ApplicantParentNrcRepository parentNrcRepository,
            EnrollmentDormRepository dormRepository,
            EnrollmentFileRepository fileRepository,
            EnrollmentMajorRepository majorRepository,
            FileTypeRepository fileTypeRepo,
            ApplicationApprovalRepository approvalRepository) {
        this.applicationRepository = applicationRepository;
        this.personalRepository = personalRepository;
        this.contactRepository = contactRepository;
        this.nrcRepository = nrcRepository;
        this.academicRepository = academicRepository;
        this.parentRepository = parentRepository;
        this.parentContactRepository = parentContactRepository;
        this.parentNrcRepository = parentNrcRepository;
        this.dormRepository = dormRepository;
        this.fileRepository = fileRepository;
        this.majorRepository = majorRepository;
        this.approvalRepository = approvalRepository;
        this.fileTypeRepo = fileTypeRepo;
    }

    @Transactional(readOnly = true)
    public EnrollmentApplicationDto getEnrollmentById(Long id) {
        EnrollmentApplication app = applicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment application not found"));

        // student
        StudentDashboardDto studentDto = null;
        Student student = app.getStudentId();
        if (student != null) {
            StudentDashboardDto sd = new StudentDashboardDto();
            sd.setId(student.getId());
            sd.setName(student.getName());
            sd.setRollNo(student.getRollNo());
            sd.setEnrolled(student.isEnrolled());
            sd.setBirthDate(student.getBirthDate());
            studentDto = sd;
        }

        //User_id
        Long user_id = student != null ? student.getUser().getId() : null;

        // personal
        ApplicantPersonalDto personalDto = personalRepository.findByApplication(app)
                .map(p -> {
                    ApplicantPersonalDto dto = new ApplicantPersonalDto();
                    dto.setId(p.getId());
                    dto.setFullNameMm(p.getFullNameMm());
                    dto.setFullNameEn(p.getFullNameEn());
                    dto.setGender(p.getGender() != null ? p.getGender().name() : null);
                    dto.setDateOfBirth(p.getDateOfBirth());
                    dto.setEthnic(p.getEthnic());
                    dto.setReligion(p.getReligion());
                    return dto;
                }).orElse(null);

        // contact
        ApplicantContactDto contactDto = contactRepository.findByApplication(app)
                .map(c -> {
                    ApplicantContactDto dto = new ApplicantContactDto();
                    dto.setId(c.getId());
                    dto.setPhoneNumber(c.getPhoneNumber());
                    dto.setUsualPhoneNumber(c.getUsualPhoneNumber());
                    dto.setEmail(c.getEmail());
                    dto.setBirthPlace(c.getBirthPlace());
                    dto.setUsualAddressRegion(c.getUsualAddressRegion());
                    dto.setUsualAddressTownship(c.getUsualAddressTownship());
                    dto.setFullAddress(c.getFullAddress());
                    return dto;
                }).orElse(null);

        // nrc
        ApplicantNrcDto nrcDto = nrcRepository.findByApplication(app)
                .map(n -> {
                    ApplicantNrcDto dto = new ApplicantNrcDto();
                    dto.setId(n.getId());
                    dto.setNrcStatus(n.getNrcStatus() != null ? n.getNrcStatus().name() : null);
                    dto.setNrc(n.getNrc());
                    dto.setNationalNrc(n.getNationalNrc());
                    return dto;
                }).orElse(null);

        // academics
        ApplicantAcademicDto academics = academicRepository.findByApplication(app)
                .map(a -> {
                    ApplicantAcademicDto dto = new ApplicantAcademicDto();
                    dto.setId(a.getId());
                    dto.setAcademicYear(a.getAcademicYear());
                    dto.setUniEntranceRollno(a.getUniEntranceRollno());
                    dto.setUniEntranceYear(a.getUniEntranceYear());
                    dto.setMajorYearId(a.getMajorYear() != null ? a.getMajorYear().getId() : null);
                    dto.setRollNo(a.getRollNo());
                    return dto;
                }).orElse(null);

        // parents (with nested contacts + nrcs)
        List<ApplicantParentDto> parents = parentRepository.findByApplication(app)
                .stream()
                .map(p -> {
                    // map parent contacts
                    ApplicantParentContactDto contacts = parentContactRepository.findByApplicantParent(p)
                                .map(pc -> {
                                    ApplicantParentContactDto cDto = new ApplicantParentContactDto();
                                    cDto.setId(pc.getId());
                                    cDto.setPhoneNumber(pc.getPhoneNumber());
                                    cDto.setUsualAddressRegionId(pc.getUsualAddressRegion() != null ? pc.getUsualAddressRegion().getId() : null);
                                    cDto.setUsualAddressTownshipId(pc.getUsualAddressTownship() != null ? pc.getUsualAddressTownship().getId() : null);
                                    cDto.setFullAddress(pc.getFullAddress());
                                    return cDto;
                                }).orElse(null);

                    // map parent nrcs
                    ApplicantParentNrcDto parentNrcs = parentNrcRepository.findByApplicantParent(p)
                                .map(pn -> {
                                    ApplicantParentNrcDto pnDto = new ApplicantParentNrcDto();
                                    pnDto.setId(pn.getId());
                                    pnDto.setNrcStatus(pn.getNrcStatus() != null ? pn.getNrcStatus().name() : null);
                                    pnDto.setNrc(pn.getNrc());
                                    pnDto.setNationalNrc(pn.getNationalNrc());
                                    return pnDto;
                                }).orElse(null);

                    ApplicantParentDto parentDto = new ApplicantParentDto();
                    parentDto.setId(p.getId());
                    parentDto.setParentType(p.getParentType() != null ? p.getParentType().name() : null);
                    parentDto.setFullNameMm(p.getFullNameMm());
                    parentDto.setFullNameEn(p.getFullNameEn());
                    parentDto.setEthnic(p.getEthnic());
                    parentDto.setReligion(p.getReligion());
                    parentDto.setOccupation(p.getOccupation());
                    parentDto.setDateOfBirth(p.getDateOfBirth());
                    parentDto.setIsDeceased(p.getIsDeceased());
                    parentDto.setDateOfDeath(p.getDateOfDeath());
                    parentDto.setNrcs(parentNrcs);
                    parentDto.setContacts(contacts);
                    return parentDto;
                }).collect(Collectors.toList());

        // dorm
        EnrollmentDormDto dormDto = dormRepository.findByApplication(app)
                .map(d -> {
                    EnrollmentDormDto dto = new EnrollmentDormDto();
                    dto.setId(d.getId());
                    dto.setDormFeeResponsible(d.getDormFeeResponsible() != null ? d.getDormFeeResponsible().name() : null);
                    dto.setOtherName(d.getOtherName());
                    dto.setOtherRelationship(d.getOtherRelationship());
                    dto.setOtherAddress(d.getOtherAddress());
                    dto.setScholarshipSupport(d.getScholarshipSupport());
                    dto.setScholarshipType(d.getScholarshipType());
                    dto.setAnotherScholarship(d.getAnotherScholarship());
                    dto.setStatus(d.getStatus());
                    return dto;
                }).orElse(null);

        // files
        List<EnrollmentFileDto> files = fileRepository.findByApplication(app)
        .stream()
        .map(f -> {
            EnrollmentFileDto dto = new EnrollmentFileDto();
            dto.setId(f.getId());
            dto.setFileName(f.getFileType() != null ? f.getFileType().getTypeName() : null);
            dto.setFileTypeId(f.getFileType() != null ? f.getFileType().getId() : null);
            dto.setFilePath(f.getFilePath());
            dto.setUploadedAt(f.getUploadedAt());
            return dto;
        }).collect(Collectors.toList());
    

        // majors
        List<EnrollmentMajorDto> majors = majorRepository.findByApplication(app)
                .stream()
                .map(m -> {
                    EnrollmentMajorDto dto = new EnrollmentMajorDto();
                    dto.setId(m.getId());
                    dto.setMajorId(m.getMajor() != null ? m.getMajor().getId() : null);
                    dto.setPreferenceRank(m.getPreferenceRank());
                    return dto;
                }).collect(Collectors.toList());

        // approvals
        List<ApplicationApprovalDto> approvals = approvalRepository.findByApplication(app)
                .stream()
                .map(a -> {
                    ApplicationApprovalDto dto = new ApplicationApprovalDto();
                    dto.setId(a.getId());
                    dto.setApproverRole(a.getApproverRole() != null ? a.getApproverRole().name() : null);
                    dto.setStatus(a.getStatus() != null ? a.getStatus().name() : null);
                    dto.setApprovedAt(a.getApprovedAt());
                    dto.setRemarks(a.getRemarks());
                    return dto;
                }).collect(Collectors.toList());

        // build and return EnrollmentApplicationDto (POJO-style)
        EnrollmentApplicationDto enrollmentDto = new EnrollmentApplicationDto();
        enrollmentDto.setId(app.getId());
        enrollmentDto.setStatus(app.getStatus() != null ? app.getStatus() : null);
        enrollmentDto.setIsFresher(app.getIsFresher());
        enrollmentDto.setCreatedAt(app.getCreatedAt());
        enrollmentDto.setUpdatedAt(app.getUpdatedAt());
        enrollmentDto.setStudent(studentDto);
        enrollmentDto.setPersonal(personalDto);
        enrollmentDto.setContact(contactDto);
        enrollmentDto.setNrc(nrcDto);
        enrollmentDto.setAcademics(academics);
        enrollmentDto.setParents(parents != null ? parents : Collections.emptyList());
        enrollmentDto.setDorm(dormDto);
        enrollmentDto.setFiles(files != null ? files : Collections.emptyList());
        enrollmentDto.setMajors(majors != null ? majors : Collections.emptyList());
        enrollmentDto.setApprovals(approvals != null ? approvals : Collections.emptyList());
        enrollmentDto.setUserId(user_id);

        return enrollmentDto;
    }
}
