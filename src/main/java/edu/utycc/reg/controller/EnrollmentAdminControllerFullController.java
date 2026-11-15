package edu.utycc.reg.controller;

import edu.utycc.reg.service.EnrollmentServiceRequest;
import edu.utycc.reg.service.PreEnrollmentOldStudentService;
import edu.utycc.reg.service.PreEnrollmentService;
import edu.utycc.reg.service.StudentService;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.utycc.reg.dto.EnrollmentApplicationDto;
import edu.utycc.reg.dto.PreEnrollmentDto;
import edu.utycc.reg.dto.PreEnrollmentOldDTO;
import edu.utycc.reg.dto.StudentDetailDto;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN') or hasRole('STUDENT_AFFAIR')")
public class EnrollmentAdminControllerFullController {
    private PreEnrollmentService preEnrollmentService;
    private EnrollmentServiceRequest enrollmentServiceRequest;
    private StudentService studentService;
    private PreEnrollmentOldStudentService preEnrollmentOldStudentService;

    public EnrollmentAdminControllerFullController(StudentService studentService,PreEnrollmentService preEnrollmentService,EnrollmentServiceRequest enrollmentServiceRequest,PreEnrollmentOldStudentService preEnrollmentOldStudentService){
        this.preEnrollmentService = preEnrollmentService;
        this.enrollmentServiceRequest = enrollmentServiceRequest;
        this.preEnrollmentOldStudentService = preEnrollmentOldStudentService;
        this.studentService = studentService;
    }

    @GetMapping("/pre-enrollments/{id}")
    public String previewById(@PathVariable Long id, ModelMap model) {
        PreEnrollmentDto dto = preEnrollmentService.getPreEnrollmentById(id);
        model.addAttribute("preEnrollment", dto);
        return "preview_pre_enrollment";
    }

    @GetMapping("/pre-enrollments_old/{id}")
    public String previewByIdOld(@PathVariable Long id, ModelMap model) {
        PreEnrollmentOldDTO dto = preEnrollmentOldStudentService.getPreEnrollmentOldById(id);
        model.addAttribute("preEnrollment", dto);
        return "preview_pre_enrollment_old";
    }
    
    @GetMapping("/enrollments/{id}")
    public String previewEnrollmentById(@PathVariable Long id, ModelMap model) {
        EnrollmentApplicationDto dto = enrollmentServiceRequest.getEnrollmentById(id);
        StudentDetailDto student = studentService.getById(dto.getStudent().getId());
        Map<Long, String> majorMap = Map.of(
            1L, "IST",
            2L, "CE",
            3L, "ECE",
            4L, "PRE",
            5L, "AME"
        );
        model.addAttribute("student",student);
        model.addAttribute("enrollment", dto);
        model.addAttribute("majorMap", majorMap);
        return "preview_enrollment";

    }  
}
