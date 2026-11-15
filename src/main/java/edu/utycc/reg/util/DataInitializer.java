// package edu.utycc.reg.util;

// import edu.utycc.reg.entity.Permission;
// import edu.utycc.reg.entity.Role;
// import edu.utycc.reg.entity.User;
// import org.springframework.web.bind.annotation.*;

// import java.util.HashSet;
// import java.util.Set;

// import org.springframework.stereotype.Component;
// import jakarta.annotation.PostConstruct;

// @Component
// public class DataInitializer {

//     private final SecurityService securityService;

//     public DataInitializer(SecurityService securityService) {
//         this.securityService = securityService;
//     }

//     @PostConstruc
//     public void init() {
//         // Create permissions
//         Permission userRead = securityService.createPermission("USER_READ", "Read user info");
//         Permission userWrite = securityService.createPermission("USER_WRITE", "Modify user info");
//         Permission roleManage = securityService.createPermission("ROLE_MANAGE", "Manage roles");
//         Permission permManage = securityService.createPermission("PERMISSION_MANAGE", "Manage permissions");
//         Permission financeView = securityService.createPermission("FINANCE_VIEW", "View finance records");
//         Permission financeEdit = securityService.createPermission("FINANCE_EDIT", "Edit finance records");
//         Permission hodApprove = securityService.createPermission("HOD_APPROVE", "Approve HOD requests");
//         Permission studentAffairManage = securityService.createPermission("STUDENT_AFFAIRS_MANAGE", "Manage student affairs");

//         // Create roles
//         Set<Permission> adminPerms = new HashSet<>();
//         adminPerms.add(userRead);
//         adminPerms.add(userWrite);
//         adminPerms.add(roleManage);
//         adminPerms.add(permManage);
//         securityService.createRole("ADMIN", "Admin role", adminPerms);

//         Set<Permission> superAdminPerms = new HashSet<>();
//         superAdminPerms.add(userRead);
//         superAdminPerms.add(userWrite);
//         superAdminPerms.add(roleManage);
//         superAdminPerms.add(permManage);
//         superAdminPerms.add(financeView);
//         superAdminPerms.add(financeEdit);
//         superAdminPerms.add(hodApprove);
//         superAdminPerms.add(studentAffairManage);
//         securityService.createRole("SUPERADMIN", "Super admin role", superAdminPerms);

//         Set<Permission> hodPerms = new HashSet<>();
//         hodPerms.add(hodApprove);
//         hodPerms.add(userRead);
//         securityService.createRole("HOD", "Head of Department", hodPerms);

//         Set<Permission> studentAffairPerms = new HashSet<>();
//         studentAffairPerms.add(studentAffairManage);
//         studentAffairPerms.add(userRead);
//         securityService.createRole("STUDENT_AFFAIR", "Student Affair role", studentAffairPerms);

//         Set<Permission> financePerms = new HashSet<>();
//         financePerms.add(financeView);
//         financePerms.add(financeEdit);
//         securityService.createRole("FINANCE", "Finance role", financePerms);
//         securityService.createRoleWithoutPerm("STUDENT", "Student role");

//     }
// }
