package edu.utycc.reg.controller;

import edu.utycc.reg.entity.PreEnrollment;
import edu.utycc.reg.service.PreEnrollmentAcceptService;
import edu.utycc.reg.service.PreEnrollmentService;
import edu.utycc.reg.dto.AcceptResponsePreEnroll;
import edu.utycc.reg.dto.RequestPreEnrollmentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/pre-enrollments")
@PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN') or hasRole('STUDENT_AFFAIR')")
public class PreEnrollmentAdminController {

    private final PreEnrollmentService preEnrollmentService;
    private final PreEnrollmentAcceptService acceptService;

    public PreEnrollmentAdminController(PreEnrollmentService preEnrollmentService,
            PreEnrollmentAcceptService acceptService) {
        this.preEnrollmentService = preEnrollmentService;
        this.acceptService = acceptService;
    }

    // -----------------------
    // CRUD Endpoints
    // -----------------------
    @PostMapping("/{id}/accept")
    public ResponseEntity<AcceptResponsePreEnroll> accept(@PathVariable("id") Long id) {
        AcceptResponsePreEnroll resp = acceptService.acceptPreEnrollment(id);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/old/{id}/accept")
    public ResponseEntity<AcceptResponsePreEnroll> acceptOld(@PathVariable("id") Long id) {
        AcceptResponsePreEnroll resp = acceptService.acceptPreEnrollmentOld(id);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<AcceptResponsePreEnroll> reject(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        if (reason == null || reason.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new AcceptResponsePreEnroll("INVALID_REASON", "Reject reason is required"));
        }
        AcceptResponsePreEnroll result = acceptService.rejectPreEnrollment(id, reason);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/old/{id}/reject")
    public ResponseEntity<AcceptResponsePreEnroll> rejectOld(
            @PathVariable("id") Long id,
            @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        if (reason == null || reason.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new AcceptResponsePreEnroll("INVALID_REASON", "Reject reason is required"));
        }
        AcceptResponsePreEnroll result = acceptService.rejectPreEnrollmentOld(id, reason);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping
    public ResponseEntity<List<PreEnrollment>> getAll() {
        return ResponseEntity.ok(preEnrollmentService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreEnrollment> update(@PathVariable Long id,
            @RequestBody RequestPreEnrollmentDTO preEnrollmentDto) {
        return ResponseEntity.ok(preEnrollmentService.update(id, preEnrollmentDto.toEntity()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        preEnrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // -----------------------
    // Custom Actions
    // -----------------------

    @PatchMapping("/{id}/status")
    public ResponseEntity<PreEnrollment> updateStatus(
            @PathVariable Long id,
            @RequestParam PreEnrollment.Status status) {
        return ResponseEntity.ok(preEnrollmentService.updateStatus(id, status));
    }

    @PatchMapping("/{id}/informed")
    public ResponseEntity<PreEnrollment> markInformed(@PathVariable Long id) {
        return ResponseEntity.ok(preEnrollmentService.markInformed(id));
    }

}
