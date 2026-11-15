package edu.utycc.reg.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.utycc.reg.entity.PreEnrollment;
import edu.utycc.reg.service.TrackingService;
import jakarta.persistence.EntityNotFoundException;

@RestController
public class TrackingController {

    private final TrackingService trackingService;

    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }
    
    @PostMapping("/check-tracking-pin")
    public ResponseEntity<Map<String, String>> checkTrackingPin(@RequestBody Map<String, String> body) {
        String pin = body.get("pin");

        if (pin == null || pin.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Pin is required"));
        }

        try {
            PreEnrollment preEnrollment = trackingService.checkByPin(pin);

            Map<String, String> response = new HashMap<>();
            response.put("status", preEnrollment.getStatus().name().toLowerCase());
            response.put("reason", preEnrollment.getRemarks() != null ? preEnrollment.getRemarks() : "");

            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Tracking pin not found"));
        }
    }
}
