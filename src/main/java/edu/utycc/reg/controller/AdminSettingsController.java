package edu.utycc.reg.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import edu.utycc.reg.dto.FileUploadResponse;
import edu.utycc.reg.entity.EnrollmentAdminSetting;
import edu.utycc.reg.entity.PaymentAdminSetting;
import edu.utycc.reg.service.EnrollmentAdminSettingService;
import edu.utycc.reg.service.FileStorageService;
import edu.utycc.reg.service.PaymentAdminSettingService;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/admin/settings")
public class AdminSettingsController {

    private final PaymentAdminSettingService paymentService;
    private final EnrollmentAdminSettingService enrollmentService;
    private final FileStorageService fileStorageService;

    public AdminSettingsController(PaymentAdminSettingService paymentService,
            FileStorageService fileStorageService,
            EnrollmentAdminSettingService enrollmentService) {
        this.paymentService = paymentService;
        this.enrollmentService = enrollmentService;
        this.fileStorageService = fileStorageService;
    }

    // --- Payment ---
    @GetMapping("/payment")
    public PaymentAdminSetting getPaymentSettings() {
        return paymentService.getSettings();
    }

    @PutMapping("/payment")
    public PaymentAdminSetting updatePaymentSettings(@RequestBody PaymentAdminSetting settings) {
        return paymentService.updateSettings(settings);
    }

    @PostMapping(value = "/payment/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileUploadResponse> uploadPaymentQr(@RequestPart("file") MultipartFile file) {
        try {
            String publicPath = fileStorageService.store(file);

            // Persist path to payment settings
            paymentService.updateQrPath(publicPath);

            return ResponseEntity.ok(new FileUploadResponse(publicPath));
        } catch (ResponseStatusException ex) {
            // forward meaningful client error
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                    "Upload failed", ex);
        }
    }

    // --- Enrollment ---
    @GetMapping("/enrollment")
    public EnrollmentAdminSetting getEnrollmentSettings() {
        return enrollmentService.getSettings();
    }

    @PutMapping("/enrollment")
    public EnrollmentAdminSetting updateEnrollmentSettings(@RequestBody EnrollmentAdminSetting settings) {
        return enrollmentService.updateSettings(settings);
    }
}
