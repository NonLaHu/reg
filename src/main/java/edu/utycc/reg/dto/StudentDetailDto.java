package edu.utycc.reg.dto;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class StudentDetailDto {
    private Long id;
    private String name;
    private String rollNo;
    private boolean enrolled;
    private LocalDate birthDate;
    private Integer age;
    private LocalDateTime createdAt;

    private PreEnrollmentDto preEnrollment;
    private PreEnrollmentOldDTO preEnrollmentOldDTO;
    private UserSummaryDto user;
    private MajorDto major;
    private MajorYearDto semester;
    private List<EnrollmentApplicationDto> enrollmentApplications;
    
    public Long getId() {
        return id;
    }
    public StudentDetailDto setId(Long id) {
        this.id = id;
        return this;
    }
    public String getName() {
        return name;
    }
    public StudentDetailDto setName(String name) {
        this.name = name;
        return this;
    }
    public String getRollNo() {
        return rollNo;
    }
    public StudentDetailDto setRollNo(String rollNo) {
        this.rollNo = rollNo;
        return this;
    }
    public boolean isEnrolled() {
        return enrolled;
    }
    public StudentDetailDto setEnrolled(boolean enrolled) {
        this.enrolled = enrolled;
        return this;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public StudentDetailDto setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }
    public Integer getAge() {
        return age;
    }
    public StudentDetailDto setAge(Integer age) {
        this.age = age;
        return this;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public StudentDetailDto setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    public PreEnrollmentDto getPreEnrollment() {
        return preEnrollment;
    }
    public StudentDetailDto setPreEnrollment(PreEnrollmentDto preEnrollment) {
        this.preEnrollment = preEnrollment;
        return this;
    }
    public UserSummaryDto getUser() {
        return user;
    }
    public StudentDetailDto setUser(UserSummaryDto user) {
        this.user = user;
        return this;
    }
    public MajorDto getMajor() {
        return major;
    }
    public StudentDetailDto setMajor(MajorDto major) {
        this.major = major;
        return this;
    }
    public MajorYearDto getSemester() {
        return semester;
    }
    public StudentDetailDto setSemester(MajorYearDto semester) {
        this.semester = semester;
        return this;
    }
    public List<EnrollmentApplicationDto> getEnrollmentApplications() {
        return enrollmentApplications;
    }
    public StudentDetailDto setEnrollmentApplications(List<EnrollmentApplicationDto> enrollmentApplications) {
        this.enrollmentApplications = enrollmentApplications;
        return this;
    }
    public PreEnrollmentOldDTO getPreEnrollmentOldDTO() {
        return preEnrollmentOldDTO;
    }
    public StudentDetailDto setPreEnrollmentOldDTO(PreEnrollmentOldDTO preEnrollmentOldDTO) {
        this.preEnrollmentOldDTO = preEnrollmentOldDTO;
        return this;
    }

    

    // getters / setters / builder constructors omitted for brevity
}