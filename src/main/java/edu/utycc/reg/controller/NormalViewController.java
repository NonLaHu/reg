package edu.utycc.reg.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import edu.utycc.reg.dto.StudentDetailDto;
import edu.utycc.reg.entity.User;
import edu.utycc.reg.security.CustomUserDetails;
import edu.utycc.reg.service.EnrollmentAdminSettingService;
import edu.utycc.reg.service.EnrollmentCheckService;
import edu.utycc.reg.service.PaymentAdminSettingService;
import edu.utycc.reg.service.StudentService;

@Controller
public class NormalViewController {
    private final StudentService studentService;
    private final EnrollmentAdminSettingService enrollSettingService;
    private final PaymentAdminSettingService paymentAdminSettingService;
    private final EnrollmentCheckService enrollmentCheckService;

    public NormalViewController(StudentService studentService, EnrollmentCheckService enrollmentCheckService, EnrollmentAdminSettingService enrollSettingService, PaymentAdminSettingService paymentAdminSettingService) {
        this.studentService = studentService;
        this.enrollSettingService = enrollSettingService;
        this.paymentAdminSettingService = paymentAdminSettingService;
        this.enrollmentCheckService = enrollmentCheckService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping({ "/user_dashboard" })
    public String redirectToDashboard(ModelMap model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        StudentDetailDto dto = studentService.getByUserId(user.getId());
        model.addAttribute("student", dto);
        return "student_dashboard";
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping({ "/new_enrollment_form" })
    public String redirectToStudentNewForm(ModelMap model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (!enrollmentCheckService.isEnrollmentOpen()) {
            return "currentlyClosed";
        }
        User user = userDetails.getUser();
        StudentDetailDto dto = studentService.getByUserId(user.getId());

        model.addAttribute("enrollmentSetting", enrollSettingService.getEnrollmentSetting());
        model.addAttribute("paymentSetting", paymentAdminSettingService.getPaymentSetting());
        model.addAttribute("student", dto);
        return "new_enrollment_form";
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping({ "/old_enrollment_form" })
    public String redirectToStudentOldForm(ModelMap model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (!enrollmentCheckService.isEnrollmentOpen()) {
            return "currentlyClosed"; // Return view if enrollment is closed
        }
        User user = userDetails.getUser();
        StudentDetailDto dto = studentService.getByUserId(user.getId());
        model.addAttribute("enrollmentSetting", enrollSettingService.getEnrollmentSetting());
        model.addAttribute("paymentSetting", paymentAdminSettingService.getPaymentSetting());
        model.addAttribute("student", dto);
        return "old_enrollment_form";
    }
}
