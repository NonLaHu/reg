// src/main/java/edu/utycc/reg/controller/UploadController.java
package edu.utycc.reg.controller;

import edu.utycc.reg.security.CustomUserDetails; // adjust package if different
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    private final Path uploadsRoot;

    public UploadController(@Value("${app.upload.dir:uploads}") String uploadsDir) {
        // Either absolute path or relative to application working dir
        this.uploadsRoot = Paths.get(uploadsDir).toAbsolutePath().normalize();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN') ")
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(
            @PathVariable String filename,
            @RequestParam(name = "download", required = false, defaultValue = "false") boolean download,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // Require authenticated user
        if (userDetails == null || userDetails.getUser() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        // Basic filename validation to prevent traversal
        if (filename.contains("..") || filename.contains("\0")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file name");
        }

        try {
            Path file = uploadsRoot.resolve(filename).normalize();

            // Ensure resolved path is still inside uploadsRoot
            if (!file.startsWith(uploadsRoot)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file path");
            }

            if (!Files.exists(file) || !Files.isRegularFile(file)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
            }

            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not readable");
            }

            // Determine content type
            String contentType = null;
            try {
                contentType = Files.probeContentType(file);
            } catch (IOException ignored) { /* fall through to default */ }

            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            if (contentType != null && !contentType.isBlank()) {
                try {
                    mediaType = MediaType.parseMediaType(contentType);
                } catch (InvalidMediaTypeException ignored) {
                    mediaType = MediaType.APPLICATION_OCTET_STREAM;
                }
            }

            HttpHeaders headers = new HttpHeaders();
            // inline for viewing in browser, attachment for forced download
            String disposition = download ? "attachment" : "inline";
            headers.setContentDisposition(ContentDisposition.builder(disposition)
                    .filename(resource.getFilename(), java.nio.charset.StandardCharsets.UTF_8)
                    .build());

            headers.setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(mediaType)
                    .contentLength(Files.size(file))
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid file URL", e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read file", e);
        }
    }
}
