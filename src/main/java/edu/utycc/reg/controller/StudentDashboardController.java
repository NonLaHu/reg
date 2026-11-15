package edu.utycc.reg.controller;

import edu.utycc.reg.dto.PasswordRequest;
import edu.utycc.reg.dto.StudentDashboardDto;
import edu.utycc.reg.entity.User;
import edu.utycc.reg.service.PasswordService;
import edu.utycc.reg.service.StudentDashboardService;
import edu.utycc.reg.security.CustomUserDetails;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentDashboardController {

    private final StudentDashboardService studentDashboardService;
    private final PasswordService passwordService;

    public StudentDashboardController(StudentDashboardService studentDashboardService, PasswordService passwordService) {
        this.studentDashboardService = studentDashboardService;
        this.passwordService = passwordService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/dashboard")
    public StudentDashboardDto getDashboard(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return studentDashboardService.getStudentDashboard(user);
    }

    @PostMapping("/api/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        boolean updated = passwordService.resetPassword(user.getUsername(), request.getOldPassword(),
                request.getNewPassword());
        if (updated) {
            return ResponseEntity.ok("Password updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid old password.");
        }
    }



}
