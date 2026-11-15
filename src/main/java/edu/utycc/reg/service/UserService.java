package edu.utycc.reg.service;

import edu.utycc.reg.repository.RoleRepository;
import edu.utycc.reg.repository.UserRepository;
import edu.utycc.reg.entity.User;
import edu.utycc.reg.dto.RecentUserDTO;
import edu.utycc.reg.dto.UserDto;
import edu.utycc.reg.entity.Role;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    public UserDto getById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found with id " + id));

        return new UserDto(user); 
    }


    public User addTestUser(String fullName, String email, String password, 
                            User.UserType userType, Set<Role> roles) {
        User user = new User();
        user.setFullName(fullName)
            .setEmail(email)
            .setPasswordHash(passwordEncoder.encode(password))
            .setUserType(userType)
            .setRoles(roles);
        return userRepository.save(user);
    }


    public void grantRole(Long id, String roleName) {
        checkNotSelf(id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        user.getRoles().add(role);
        userRepository.save(user);
    }

    public void revokeRole(Long id, String roleName) {
        checkNotSelf(id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.getRoles().removeIf(r -> r.getName().equals(roleName));
        userRepository.save(user);
    }

    public void resetPassword(Long id, String newPassword) {
        checkNotSelf(id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Optional: send email notification
    }

    // Utility method to prevent self-modification
    private void checkNotSelf(Long targetUserId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User currentUser) {
            if (currentUser.getId().equals(targetUserId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Action on yourself is not allowed!");
            }
        }
    }

    public List<RecentUserDTO> getRecentUsers(int limit) {
        return userRepository.findAllOrderByCreatedAtDesc()
                .stream()
                .limit(limit)
                .map(RecentUserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<User> getUsers(String filter, Pageable pageable) {
        if (filter == null || filter.isBlank()) {
            return userRepository.findAll(pageable);
        } else {
            return userRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(filter, filter, pageable);
        }
    }

    public long countUsers() {
        return userRepository.count();
    }
}