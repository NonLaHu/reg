package edu.utycc.reg.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileController {

    private final Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
    private final Path uploadDirPayment = Paths.get("uploads/payment").toAbsolutePath().normalize();

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN') or hasAuthority('USER_READ')")
    @GetMapping("/files/uploads/{filepath:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filepath) throws Exception {
        // Resolve safely inside uploads/
        Path target = uploadDir.resolve(filepath).normalize();

        // Block access if someone tries to escape the uploads directory
        if (!target.startsWith(uploadDir)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Resource resource = new FileSystemResource(target);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(target))
                .body(resource);
    }

    @GetMapping("/public/uploads/payment/{filepath:.+}")
    public ResponseEntity<Resource> serveFilePayment(@PathVariable String filepath) throws Exception {
        // Resolve safely inside uploads/
        Path target = uploadDirPayment.resolve(filepath).normalize();

        // Block access if someone tries to escape the uploads directory
        if (!target.startsWith(uploadDirPayment)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Resource resource = new FileSystemResource(target);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(target))
                .body(resource);
    }
}
