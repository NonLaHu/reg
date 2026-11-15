package edu.utycc.reg.controller;

import edu.utycc.reg.util.ApiResponse;
import edu.utycc.reg.entity.PreEnrollment;
import edu.utycc.reg.entity.User;
import edu.utycc.reg.security.CustomUserDetails;
import edu.utycc.reg.service.AdminEnrollmentService;
import edu.utycc.reg.service.EnrollmentCheckService;
import edu.utycc.reg.service.EnrollmentService;
import edu.utycc.reg.service.FileStorageService;
import edu.utycc.reg.service.PreEnrollmentOldStudentService;
import edu.utycc.reg.service.PreEnrollmentService;
import edu.utycc.reg.util.FileValidator;
import jakarta.persistence.EntityNotFoundException;
import edu.utycc.reg.dto.EnrollmentRequestFresher;
import edu.utycc.reg.dto.FieldUpdateRequest;
import edu.utycc.reg.dto.RequestPreEnrollmentDTO;
import edu.utycc.reg.dto.RequestPreEnrollmentOldDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EnrollmentController {

    private final PreEnrollmentService preEnrollmentService;
    private final EnrollmentService enrollmentService;
    private final AdminEnrollmentService service;
    private final FileStorageService fileStorageService;
    private final PreEnrollmentOldStudentService preEnrollmentOldStudentService;
    private final EnrollmentCheckService enrollmentCheckService;

    public EnrollmentController(PreEnrollmentOldStudentService preEnrollmentOldStudentService,
            PreEnrollmentService preEnrollmentService, EnrollmentService enrollmentService,
            AdminEnrollmentService service, FileStorageService fileStorageService,
            EnrollmentCheckService enrollmentCheckService) {
        this.enrollmentService = enrollmentService;
        this.preEnrollmentService = preEnrollmentService;
        this.service = service;
        this.fileStorageService = fileStorageService;
        this.preEnrollmentOldStudentService = preEnrollmentOldStudentService;
        this.enrollmentCheckService = enrollmentCheckService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping(value = "/enrollments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> acceptEnrollment(
            @RequestBody EnrollmentRequestFresher request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (!enrollmentCheckService.isEnrollmentOpen()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Enrollment is currently closed");
        }
        User user = userDetails.getUser();

        enrollmentService.acceptEnrollment(
                request.getDto(),
                request.getFiles(),
                user.getUsername());

        return ResponseEntity.ok(ApiResponse.success("Enrollment successful"));
    }

    @PostMapping("/pre_enrollments")
    public ResponseEntity<?> createPreEnrollment(
            @RequestPart("dto") RequestPreEnrollmentDTO dto,
            @RequestPart("passCertificate") MultipartFile file) throws IOException {

        FileValidator.validateImage(file);

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploads", filename);

        try {
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            PreEnrollment entity = dto.toEntity();
            entity.setPassCertificate(path.toString());

            PreEnrollment saved = preEnrollmentService.create(entity);

            // ✅ Return consistent JSON with status: "ok"
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "status", "ok",
                            "message", "Pre-enrollment successful",
                            "trackingPin", saved.getTrackingPin()));

        } catch (Exception e) {
            // Delete the file if anything fails
            try {
                Files.deleteIfExists(path);
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
            e.printStackTrace();

            // ✅ Return consistent JSON with status: "error"
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Pre-enrollment creation failed: " + e.getMessage()));
        }
    }

    @PostMapping("/pre_enrollments_old")
    public ResponseEntity<?> createPreEnrollmentOld(
            @RequestPart("dto") RequestPreEnrollmentOldDTO dto,
            @RequestPart("studentIdCardUrl") MultipartFile file) throws IOException {

        FileValidator.validateImage(file);

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploads", filename);

        try {
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            preEnrollmentOldStudentService.create(dto, path.toString());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Pre-enrollment (old) created successfully"));

        } catch (Exception e) {
            try {
                Files.deleteIfExists(path);
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Pre-enrollment (old) creation failed: " + e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @PostMapping("/admin/enrollments/{id}/accept")
    public ResponseEntity<ApiResponse<?>> accept(@PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required"));
        }
        try {
            service.acceptEnrollment(id, userDetails.getUser().getEmail());
            return ResponseEntity.ok(ApiResponse.success("Enrollment accepted"));
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @PostMapping("/admin/enrollments/{id}/reject")
    public ResponseEntity<ApiResponse<?>> reject(@PathVariable("id") Long id,
            @RequestParam(name = "remark", required = false) String remark,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication required"));
        }
        try {
            service.rejectEnrollment(id, remark, userDetails.getUser().getEmail());
            return ResponseEntity.ok(ApiResponse.success("Enrollment rejected"));
        } catch (Exception ex) {
            return ResponseEntity.status(400).body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PatchMapping("/admin/enrollments/{id}/field")
    public ResponseEntity<?> updateField(
            @PathVariable Long id,
            @RequestBody FieldUpdateRequest request) {
        try {
            enrollmentService.updateField(id, request.getPath(), request.getValue(), request.getEntityId());
            return ResponseEntity.ok(Map.of("message", "Updated"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Unexpected error: " + e.getMessage()));
        }
    }

    @PatchMapping("/admin/pre-enrollments/{id}/field")
    public ResponseEntity<?> updatePreEnrollmentField(
            @PathVariable Long id,
            @RequestBody FieldUpdateRequest request) {
        try {
            preEnrollmentService.updateField(id, request.getPath(), request.getValue());//i need this service
            return ResponseEntity.ok(Map.of("message", "Updated"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Unexpected error: " + e.getMessage()));
        }
    }

    @PatchMapping("/admin/pre-enrollments-old/{id}/field")
    public ResponseEntity<?> updatePreEnrollmentOldField(
            @PathVariable Long id,
            @RequestBody FieldUpdateRequest request) {
        try {
            preEnrollmentOldStudentService.updateField(id, request.getPath(), request.getValue());//i need this service
            return ResponseEntity.ok(Map.of("message", "Updated"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Unexpected error: " + e.getMessage()));
        }
    }
}
