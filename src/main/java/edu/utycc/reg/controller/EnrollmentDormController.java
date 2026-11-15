package edu.utycc.reg.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.utycc.reg.service.EnrollmentDormService;

@RestController
@RequestMapping("/api/dorm")
@PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN') or hasRole('STUDENT_AFFAIR')")
public class EnrollmentDormController {

    private final EnrollmentDormService dormService;

    public EnrollmentDormController(EnrollmentDormService dormService) {
        this.dormService = dormService;
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptDorm(@PathVariable("id") Long id) {
        try {
            Map<String, String> result = dormService.acceptDorm(id);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectDorm(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        if (reason == null || reason.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Reject reason is required"));
        }

        try {
            Map<String, String> result = dormService.rejectDorm(id, reason);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
