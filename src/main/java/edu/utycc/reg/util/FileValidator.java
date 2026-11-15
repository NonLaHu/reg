package edu.utycc.reg.util;

import javax.imageio.ImageIO;
import org.springframework.web.multipart.MultipartFile;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import edu.utycc.reg.exception.FileValidationException;

public class FileValidator {

    private static final long MAX_FILE_SIZE = 3 * 1024 * 1024; // 3MB

    public static void validateImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new FileValidationException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileValidationException("File exceeds max size of 500KB");
        }

        // Check extension
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.matches("(?i).*\\.(jpg|jpeg|png|gif)$")) {
            throw new FileValidationException("Invalid file extension. Only jpg, jpeg, png, gif allowed.");
        }

        // Check MIME type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new FileValidationException("Invalid content type. Must be an image.");
        }

        // Check magic numbers / file signature
        try (InputStream is = file.getInputStream()) {
            byte[] header = new byte[8];
            if (is.read(header, 0, header.length) < header.length) {
                throw new FileValidationException("Cannot read file header");
            }
            if (!isValidImage(header)) {
                throw new FileValidationException("File content does not match an image");
            }
        }

        // Optional: check if Java can parse image
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new FileValidationException("Uploaded file is not a valid image");
        }
    }

    private static boolean isValidImage(byte[] header) {
        // PNG: 89 50 4E 47 0D 0A 1A 0A
        if (header[0] == (byte)0x89 && header[1] == 0x50 && header[2] == 0x4E && header[3] == 0x47) return true;
        // JPEG: FF D8
        if (header[0] == (byte)0xFF && header[1] == (byte)0xD8) return true;
        // GIF: 47 49 46 38
        if (header[0] == 0x47 && header[1] == 0x49 && header[2] == 0x46 && header[3] == 0x38) return true;

        return false;
    }
}
