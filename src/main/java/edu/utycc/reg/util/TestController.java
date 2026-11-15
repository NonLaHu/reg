// package edu.utycc.reg.util;

// import edu.utycc.reg.entity.Role;
// import edu.utycc.reg.entity.User;
// import edu.utycc.reg.service.UserService;
// import jakarta.annotation.PostConstruct;
// import org.springframework.stereotype.Component;

// import java.util.HashSet;
// import java.util.Set;

// @Component
// public class TestController {

//     private final UserService userService;
//     private final SecurityService securityService;

//     public TestController(UserService userService, SecurityService securityService) {
//         this.userService = userService;
//         this.securityService = securityService;
//     }

//     @PostConstruc
//     public void initUsersAndRoles() {
//         // Fetch roles from DB (assume already created)
//         Role adminRole = securityService.roleRepository.findByName("ADMIN").orElseThrow();
//         Role superAdminRole = securityService.roleRepository.findByName("SUPERADMIN").orElseThrow();
//         Role hodRole = securityService.roleRepository.findByName("HOD").orElseThrow();
//         Role studentAffairRole = securityService.roleRepository.findByName("STUDENT_AFFAIR").orElseThrow();
//         Role financeRole = securityService.roleRepository.findByName("FINANCE").orElseThrow();

//         // Create test users with user types and roles
//         userService.addTestUser("Alice Admin", "alice.admin@example.com", "1234", User.UserType.ADMIN, Set.of(adminRole));
//         userService.addTestUser("Bob SuperAdmin", "bob.superadmin@example.com", "1234", User.UserType.ADMIN, Set.of(superAdminRole));
//         userService.addTestUser("Charlie HOD", "charlie.hod@example.com", "1234", User.UserType.ADMIN, Set.of(hodRole));
//         userService.addTestUser("Diana StudentAffair", "diana.sa@example.com", "1234", User.UserType.ADMIN, Set.of(studentAffairRole));
//         userService.addTestUser("Eve Finance", "eve.finance@example.com", "1234", User.UserType.ADMIN, Set.of(financeRole));

//         // User with multiple roles
//         Set<Role> multiRoles = new HashSet<>();
//         multiRoles.add(adminRole);
//         multiRoles.add(financeRole);
//         userService.addTestUser("Frank MultiRole", "frank.multirole@example.com", "1234", User.UserType.ADMIN, multiRoles);

//         // Example student users
//         Role studentRole = securityService.roleRepository.findByName("STUDENT").orElseThrow();
//         userService.addTestUser("Grace Student", "grace.student@example.com", "1234", User.UserType.STUDENT, Set.of(studentRole));
//         userService.addTestUser("Henry Student", "henry.student@example.com", "1234", User.UserType.STUDENT, Set.of(studentRole));
//     }
// }
