package edu.utycc.reg.dto;

import edu.utycc.reg.entity.EnrollmentDorm;
import edu.utycc.reg.entity.EnrollmentApplication.Status;

import java.time.LocalDateTime;
import java.util.List;

public class EnrollmentApplicationDto {
    private Boolean isFresher;
    private ApplicantAcademicDto academics;
    private ApplicantContactDto contact;
    private ApplicantNrcDto nrc;
    private ApplicantPersonalDto personal;
    private List<ApplicantParentDto> parents;
    private EnrollmentDormDto dorm;
    private List<EnrollmentFileDto> files;
    private List<EnrollmentMajorDto> majors;
    private StudentDashboardDto student;
    private List<ApplicationApprovalDto> approvals;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status status;
    private Long userId;
    private Long id;
    private String remark;
    private EnrollmentDorm.Status dormStatus;

    
    
    public Status getStatus() {
        return status;
    }
    public EnrollmentApplicationDto setStatus(Status status) {
        this.status = status;
        return this;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public EnrollmentApplicationDto setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    public Boolean getIsFresher() {
        return isFresher;
    }
    public EnrollmentApplicationDto setIsFresher(Boolean isFresher) {
        this.isFresher = isFresher;
        return this;
    }
    public ApplicantContactDto getContact() {
        return contact;
    }
    public EnrollmentApplicationDto setContact(ApplicantContactDto contact) {
        this.contact = contact;
        return this;
    }
    public ApplicantNrcDto getNrc() {
        return nrc;
    }
    public EnrollmentApplicationDto setNrc(ApplicantNrcDto nrc) {
        this.nrc = nrc;
        return this;
    }
    public ApplicantPersonalDto getPersonal() {
        return personal;
    }
    public EnrollmentApplicationDto setPersonal(ApplicantPersonalDto personal) {
        this.personal = personal;
        return this;
    }
    public List<ApplicantParentDto> getParents() {
        return parents;
    }
    public EnrollmentApplicationDto setParents(List<ApplicantParentDto> parents) {
        this.parents = parents;
        return this;
    }
    public EnrollmentDormDto getDorm() {
        return dorm;
    }
    public EnrollmentApplicationDto setDorm(EnrollmentDormDto dorm) {
        this.dorm = dorm;
        return this;
    }
    public List<EnrollmentFileDto> getFiles() {
        return files;
    }
    public EnrollmentApplicationDto setFiles(List<EnrollmentFileDto> files) {
        this.files = files;
        return this;
    }
    public List<EnrollmentMajorDto> getMajors() {
        return majors;
    }
    public EnrollmentApplicationDto setMajors(List<EnrollmentMajorDto> majors) {
        this.majors = majors;
        return this;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public EnrollmentApplicationDto setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
    public StudentDashboardDto getStudent() {
        return student;
    }
    public EnrollmentApplicationDto setStudent(StudentDashboardDto student) {
        this.student = student;
        return this;
    }
    public Long getId() {
        return id;
    }
    public EnrollmentApplicationDto setId(Long id) {
        this.id = id;
        return this;
    }
    public List<ApplicationApprovalDto> getApprovals() {
        return approvals;
    }
    public EnrollmentApplicationDto setApprovals(List<ApplicationApprovalDto> approvals) {
        this.approvals = approvals;
        return this;
    }
    public Long getUserId() {
        return userId;
    }
    public EnrollmentApplicationDto setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
    public ApplicantAcademicDto getAcademics() {
        return academics;
    }
    public EnrollmentApplicationDto setAcademics(ApplicantAcademicDto academics) {
        this.academics = academics;
        return this;
    }
    public String getRemark() {
        return remark;
    }
    public EnrollmentApplicationDto setRemark(String remark) {
        this.remark = remark;
        return this;
    }
    public EnrollmentDorm.Status getDormStatus() {
        return dormStatus;
    }
    public EnrollmentApplicationDto setDormStatus(EnrollmentDorm.Status dormStatus) {
        this.dormStatus = dormStatus;
        return this;
    }

    

}
