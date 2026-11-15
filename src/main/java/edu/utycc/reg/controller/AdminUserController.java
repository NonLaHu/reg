package edu.utycc.reg.controller;

import edu.utycc.reg.entity.User;
import edu.utycc.reg.service.AdminUserService;
import edu.utycc.reg.service.UserService;
import edu.utycc.reg.dto.RoleDto;
import edu.utycc.reg.dto.UserDto;
import edu.utycc.reg.security.CustomUserDetails;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;
    private final UserService userService;

    public AdminUserController(AdminUserService adminUserService, UserService userService) {
        this.adminUserService = adminUserService;
        this.userService = userService;
    }

    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> resetPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) { // accept JSON payload
        String newPassword = body.get("newPassword"); // superadmin-provided password
        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "New password must not be empty"));
        }

        userService.resetPassword(id, newPassword);
        return ResponseEntity.ok(Map.of(
                "message", "Password has been reset successfully.",
                "userId", id));
    }

    @PostMapping("/{id}/roles/{role}/grant")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> grantRole(@PathVariable Long id, @PathVariable String role) {
        userService.grantRole(id, role);
        return ResponseEntity.ok(Map.of(
                "message", "Role granted successfully.",
                "userId", id,
                "role", role));
    }

    @PostMapping("/{id}/roles/{role}/revoke")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> revokeRole(@PathVariable Long id, @PathVariable String role) {
        userService.revokeRole(id, role);
        return ResponseEntity.ok(Map.of(
                "message", "Role revoked successfully.",
                "userId", id,
                "role", role));
    }

    // List all users
    @GetMapping
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN') or hasAuthority('USER_READ')")
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(adminUserService.getAllUsers(pageable));
    }

    // Get single user
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN') or hasAuthority('USER_READ')")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return adminUserService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> payload) {
        try {
            User createdUser = adminUserService.createUser(payload);
            return ResponseEntity.ok(Map.of(
                    "message", "User created successfully",
                    "fullName", createdUser.getFullName(),
                    "email", createdUser.getEmail()));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // Update user
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(adminUserService.updateUser(id, user));
    }

    // Delete user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();

        // Fetch user with roles from DB
        UserDto userDto = userService.getById(customUser.getUser().getId());

        return ResponseEntity.ok(userDto);
    }
}
