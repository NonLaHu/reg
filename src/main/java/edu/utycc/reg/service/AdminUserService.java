package edu.utycc.reg.service;

import edu.utycc.reg.entity.Role;
import edu.utycc.reg.entity.User;
import edu.utycc.reg.repository.UserRepository;
import edu.utycc.reg.repository.RoleRepository;
import edu.utycc.reg.dto.UserDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // List all users
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserDto::new); // map User -> UserDto
    }

    // Get user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Create new user
    public User createUser(Map<String, String> payload) {

        // Check if email already exists
        if (userRepository.existsByEmail(payload.get("email"))) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFullName(payload.get("fullName"));
        user.setEmail(payload.get("email"));

        // Hash password
        user.setPasswordHash(passwordEncoder.encode(payload.get("password")));

        // Find Role entity
        Role role = roleRepository.findByName(payload.get("role"))
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role);

        // Set userType based on role
        if ("STUDENT".equalsIgnoreCase(payload.get("role"))) {
            user.setUserType(User.UserType.STUDENT);
        } else {
            user.setUserType(User.UserType.ADMIN);
        }

        return userRepository.save(user);
    }

    // Update existing user
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setFullName(updatedUser.getFullName())
                            .setEmail(updatedUser.getEmail())
                            .setStatus(updatedUser.getStatus())
                            .setUserType(updatedUser.getUserType())
                            .setRoles(updatedUser.getRoles());
                    return userRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Assign role to user
    public User assignRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        return userRepository.save(user);
    }

    // Remove role from user
    public User removeRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().remove(role);
        return userRepository.save(user);
    }
}
