package edu.utycc.reg.service;

import edu.utycc.reg.entity.Permission;
import edu.utycc.reg.entity.Role;
import edu.utycc.reg.entity.User;
import edu.utycc.reg.repository.PermissionRepository;
import edu.utycc.reg.repository.RoleRepository;
import edu.utycc.reg.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.HashSet;
import java.util.Set;

@Service
public class SecurityService {

    public final UserRepository userRepository;
    public final RoleRepository roleRepository;
    public final PermissionRepository permissionRepository;
    public final PasswordEncoder passwordEncoder;

    public SecurityService(UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Permission createPermission(String code, String description) {
        Permission permission = new Permission();
        permission.setCode(code).setDescription(description);
        return permissionRepository.save(permission);
    }

    public Role createRole(String name, String description, Set<Permission> permissions) {
        Role role = new Role();
        role.setName(name).setDescription(description).setPermissions(permissions);
        return roleRepository.save(role);
    }

    public Role createRoleWithoutPerm(String name, String description) {
        Role role = new Role();
        role.setName(name).setDescription(description);
        return roleRepository.save(role);
    }

    public User addUserWithRole(String fullName, String email, String password, Role role) {
        User user = new User();
        user.setFullName(fullName)
            .setEmail(email)
            .setPasswordHash(passwordEncoder.encode(password))
            .setUserType(User.UserType.STUDENT)
            .setStatus(User.Status.ACTIVE);

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        return userRepository.save(user);
    }
}
