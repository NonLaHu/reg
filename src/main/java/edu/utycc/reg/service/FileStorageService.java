package edu.utycc.reg.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import edu.utycc.reg.util.FileValidator;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileStorageService {
    @Value("${admin.upload-dir:uploads/payment}")
    private String uploadDir; // e.g., uploads/payment

    @Value("${admin.max-file-size:5242880}") // 5MB default
    private long maxFileSize;

    // Allowed content types
    private static final String[] ALLOWED_TYPES = new String[] {
            "image/png", "image/jpeg", "image/jpg", "image/webp", "image/gif"
    };

    public String store(MultipartFile file) {
        validate(file);
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            String original = StringUtils.cleanPath(file.getOriginalFilename());
            String ext = "";

            int idx = original.lastIndexOf('.');
            if (idx > -1)
                ext = original.substring(idx);
            // generate filename: timestamp-uuid.ext
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String filename = timestamp + "-" + UUID.randomUUID().toString() + ext;

            Path target = uploadPath.resolve(filename);
            // copy file
            try (var in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }

            // Return public path used by frontend: "/uploads/payment/<filename>"
            // We assume uploadDir ends with "payment" or similar and mapping is /uploads/**
            String[] parts = uploadDir.replace("\\", "/").split("/");
            String publicPrefix = "/uploads";
            if (parts.length > 1) {
                // e.g., uploads/payment -> /uploads/payment
                publicPrefix += "/" + String.join("/", java.util.Arrays.copyOfRange(parts, 1, parts.length));
            } else if (parts.length == 1 && !parts[0].equals("uploads")) {
                publicPrefix += "/" + parts[0];
            }
            // Simpler: compute public path from uploadDir relative to "uploads" base
            // For reliability, construct as: /uploads/<rest_of_path>/<filename>
            String rest = "";
            Path uploadsRoot = Paths.get("uploads").toAbsolutePath().normalize();
            Path relative;
            try {
                relative = uploadsRoot.relativize(target.toAbsolutePath());
                rest = relative.toString().replace("\\", "/");
                return "/uploads/" + rest;
            } catch (Exception ex) {
                // fallback: just return /uploads/payment/filename
                return publicPrefix + "/" + filename;
            }

        } catch (IOException ex) {
            throw new ResponseStatusException(BAD_REQUEST, "Failed to store file", ex);
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "No file provided");
        }
        if (file.getSize() > maxFileSize) {
            throw new ResponseStatusException(BAD_REQUEST, "File size exceeds limit of " + maxFileSize + " bytes");
        }
        String contentType = file.getContentType();
        boolean ok = false;
        if (contentType != null) {
            for (String t : ALLOWED_TYPES) {
                if (t.equalsIgnoreCase(contentType)) {
                    ok = true;
                    break;
                }
            }
        }
        if (!ok) {
            throw new ResponseStatusException(BAD_REQUEST, "Unsupported file type: " + contentType);
        }
    }

    public String saveStudentIdCard(MultipartFile file) throws IOException {
        FileValidator.validateImage(file);
        String filename = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        Path dir = Paths.get("uploads", "pre_enrollment_old");
        Files.createDirectories(dir);
        Path filePath = dir.resolve(filename);
        Files.write(filePath, file.getBytes());
        return filePath.toString();
    }
}
