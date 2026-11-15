package edu.utycc.reg.util;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // exclude null fields from JSON
public record ApiResponse<T>(
        String status,
        String message,
        T data
) {
    // Factory methods for convenience
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("ok", message, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>("ok", message, null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null);
    }
}
