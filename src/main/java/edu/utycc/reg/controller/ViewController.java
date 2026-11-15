package edu.utycc.reg.controller;

import edu.utycc.reg.dto.RoleDto;
import edu.utycc.reg.dto.StudentDetailDto;
import edu.utycc.reg.dto.UserDto;
import edu.utycc.reg.entity.Announcement;
import edu.utycc.reg.service.AnnouncementService;
import edu.utycc.reg.service.RoleService;
import edu.utycc.reg.service.StudentService;
import edu.utycc.reg.service.UserService;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ViewController {

    private final StudentService studentService;
    private final UserService userService;
    private final RoleService roleService;
    private final AnnouncementService announcementService;

    public ViewController(StudentService studentService,UserService userService,RoleService roleService, AnnouncementService announcementService) {
        this.studentService = studentService;
        this.userService = userService;
        this.roleService = roleService;
        this.announcementService = announcementService;
    }

    @GetMapping("/admin-users/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public String getUserDetailPage(@PathVariable Long id, Model model) {
        UserDto userDto = userService.getById(id);
        List<RoleDto> roles = roleService.getAllRoles();

        model.addAttribute("user", userDto);
        model.addAttribute("allRoles", roles);

        return "user_detail";
    }

    @GetMapping("/announcements/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public String getAnnoucementDetailPage(@PathVariable Long id, Model model) {
        Announcement dto = announcementService.getById(id);
        model.addAttribute("announcement", dto);
        return "announcement_detail";
    }

    @GetMapping("/students/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public String getStudentDetailPage(@PathVariable Long id, Model model) {
        StudentDetailDto dto = studentService.getById(id);
        model.addAttribute("student", dto);
        return "student_detail";
    }

    @GetMapping("/announcement_admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public String getAnnoucementDetailPage( Model model) {
        return "announcement_admin";
    }
}
