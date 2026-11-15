package edu.utycc.reg.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import edu.utycc.reg.util.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Object handleAllExceptions(Exception ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        String clientIp = getClientIp(request);

        String username = "anonymous";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            username = auth.getName();
        }

        logger.error("Exception caught | User: {} | IP: {} | Method: {} | Path: {} | Exception: {}: {}",
                username, clientIp, method, path, ex.getClass().getSimpleName(), ex.getMessage());
        ex.printStackTrace();

        HttpStatus status;
        String message;

        if (ex instanceof BadCredentialsException) {
            message = "The username or password is incorrect";
            status = HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof AccountStatusException) {
            message = "The account is locked";
            status = HttpStatus.FORBIDDEN;
        } else if (ex instanceof AccessDeniedException) {
            message = "You are not authorized to access this resource";
            status = HttpStatus.FORBIDDEN;
        } else if (ex instanceof SignatureException) {
            message = "The JWT signature is invalid";
            status = HttpStatus.FORBIDDEN;
        } else if (ex instanceof ExpiredJwtException) {
            message = "The JWT token has expired";
            status = HttpStatus.FORBIDDEN;
        } else if (ex instanceof MaxUploadSizeExceededException) {
            message = "Maximum upload size exceeded";
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof FileValidationException) {
            message = ex.getMessage();
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            message = "File Type Not supported";
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof org.springframework.web.servlet.resource.NoResourceFoundException) {
            status = HttpStatus.NOT_FOUND;
            message = "Your endpoint can't be found. Make sure the URL is correct.";

            String accept = request.getHeader("Accept");
            if (accept != null && accept.contains("text/html")) {
                ModelAndView mv = new ModelAndView("not_found");
                mv.addObject("path", path);
                mv.addObject("message", message);
                return mv;
            }
        } else {
            message = "Unknown internal server error";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(ApiResponse.error(message));
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0].trim();
    }
}