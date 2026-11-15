package edu.utycc.reg.controller;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.utycc.reg.entity.Student;
import edu.utycc.reg.entity.User;
import edu.utycc.reg.repository.StudentRepository;
import edu.utycc.reg.service.PasswordService;

@RestController
@RequestMapping("/api/admin/students")
public class AdminStudentRestController {

    private final PasswordService passwordService;
    private final StudentRepository studentRepository;;

    public AdminStudentRestController( StudentRepository studentRepository,PasswordService passwordService ) {
        this.passwordService = passwordService;
        this.studentRepository = studentRepository;
    }

    @PostMapping("/student-reset-pass/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public ResponseEntity<Map<String, String>> resetStudentPassword(@PathVariable Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        User user = student.getUser();
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No linked user account found");
        }

        // Generate new system password
        String newPassword = generateRandomPassword(5);

        // Call your service
        boolean success = passwordService.resetPasswordByForce(user.getUsername(), newPassword);
        if (!success) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Password reset failed");
        }

        // Return message (avoid sending plain password in prod)
        Map<String, String> response = new HashMap<>();
        response.put("message", "New password: " + newPassword);
        return ResponseEntity.ok(response);
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
