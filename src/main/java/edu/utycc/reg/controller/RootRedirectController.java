package edu.utycc.reg.controller;

import edu.utycc.reg.dto.RoleDto;
import edu.utycc.reg.entity.Announcement;
import edu.utycc.reg.entity.NrcTownship;
import edu.utycc.reg.service.AnnouncementService;
import edu.utycc.reg.service.EnrollmentCheckService;
import edu.utycc.reg.service.MajorService;
import edu.utycc.reg.service.MajorYearService;
import edu.utycc.reg.service.NrcService;
import edu.utycc.reg.service.RoleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RootRedirectController {

    private NrcService nrcService;
    private MajorService majorService;
    private MajorYearService majorYearService;
    private RoleService roleService;
    private AnnouncementService announcementService;
    private EnrollmentCheckService enrollmentCheckService;


    public RootRedirectController(NrcService nrcService, MajorService majorService, MajorYearService majorYearService,
            RoleService roleService, AnnouncementService announcementService, EnrollmentCheckService enrollmentCheckService) {
        this.nrcService = nrcService;
        this.majorService = majorService;
        this.majorYearService = majorYearService;
        this.roleService = roleService;
        this.announcementService = announcementService;
        this.enrollmentCheckService = enrollmentCheckService;
    }

    @GetMapping({ "/" })
    public String redirectToIndex(ModelMap model) {
        model.addAttribute("recentAnnouncements", announcementService.getActiveRecentAnnouncements());
        return "index";
    }

    @GetMapping({ "/admin" })
    public String redirectToAdmin(ModelMap model) {
        return "admin";
    }

    @GetMapping({ "/signup" })
    public String redirectToSignUp() {
        return "signup";
    }

    @GetMapping({ "/admin_profile" })
    public String redirectToProfile(ModelMap model) {
        return "admin_profile";
    }

    @GetMapping({ "/render" })
    public String redirectToRenderer(ModelMap model) {
        return "render";
    }

    @GetMapping({ "/dashboard" })
    public String redirectToRendererDashboard() {
        return "dashboard";
    }

    @GetMapping({ "/render_enrollment_new" })
    public String redirectToNewEnrollmentRenderer() {
        return "render_enrollment_new";
    }

    @GetMapping({ "/not-authorized" })
    public String redirectToNotAuthorized() {
        return "not-authorized";
    }

    @GetMapping({ "/render_enrollment_old" })
    public String redirectToOldEnrollmentRenderer() {
        return "render_enrollment_old";
    }

    @GetMapping({ "/new_enrollment" })
    public String redirectToNewEnrollment() {
        if (!enrollmentCheckService.isEnrollmentOpen()) {
            return "currentlyClosed";
        }
        return "new_enrollment";
    }

    @GetMapping({ "/old_enrollment" })
    public String redirectToOldEnrollment() {
        if (!enrollmentCheckService.isEnrollmentOpen()) {
            return "currentlyClosed";
        }
        return "old_enrollment";
    }

    @GetMapping({ "/currentlyClosed" })
    public String redirectToClosed() {
        return "currentlyClosed";
    }

    @GetMapping({ "/howToApply" })
    public String redirectToHowToApply() {
        return "howToApply";
    }

    @GetMapping({ "/renderOldStudent" })
    public String redirectToOldRenderer(ModelMap model) {
        return "renderOldStudent";
    }

    @GetMapping({ "/renderAdminUser" })
    public String redirectToAdminUser(ModelMap model) {
        return "renderAdminUser";
    }

    @GetMapping({ "/render_enrollment" })
    public String redirectToForm(ModelMap model) {
        return "render_enrollment";
    }

    @GetMapping({ "/render_student_detail" })
    public String redirectToStudentDetail(ModelMap model) {
        return "render_student_detail";
    }

    @GetMapping({ "/render_preview_enrollment" })
    public String redirectToEnrollmentForm(ModelMap model) {
        return "render_preview_enrollment";
    }

    @GetMapping({ "/render_announcement_admin" })
    public String redirectToAnnouncementAdmin() {
        return "render_announcement_admin";
    }

    @GetMapping({ "/history" })
    public String redirectToHistory() {
        return "history";
    }

    @GetMapping({ "/department" })
    public String redirectToDepartment() {
        return "department";
    }

    @GetMapping("/announcement")
    public String showAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            ModelMap model) {
        
        int size = 5;
        Page<Announcement> announcements = announcementService.getActiveAnnouncementsPage(page, size);

        model.addAttribute("announcements", announcements.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", announcements.getTotalPages());

        return "announcement";
    }

    @GetMapping({ "/academic" })
    public String redirectToAcademic() {
        return "academic";
    }

    @GetMapping({ "/user_create" })
    public String redirectToUserCreation(ModelMap model) {
        List<RoleDto> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "user_create";
    }

    @GetMapping({ "/render_announcement_detail" })
    public String redirectToAnnouncement() {
        return "render_announcement_detail";
    }

    @GetMapping({ "/pre_enrollment_old" })
    public String redirectToPreEnrollmentOld(ModelMap model) {
        List<NrcTownship> all = nrcService.getAllTownships();

        Map<String, List<NrcTownship>> grouped = all.stream().collect(Collectors.groupingBy(NrcTownship::getStateCode));
        model.addAttribute("majors", majorService.getAllMajors());
        model.addAttribute("majorYears", majorYearService.findAll());
        model.addAttribute("groupedTownships", grouped);
        model.addAttribute("nrcTypes", nrcService.getAllTypes());
        return "pre_enrollment_old";
    }

    @GetMapping({ "/pre_enrollment" })
    public String redirectToPreEnrollment(ModelMap model) {
        List<NrcTownship> all = nrcService.getAllTownships();

        Map<String, List<NrcTownship>> grouped = all.stream().collect(Collectors.groupingBy(NrcTownship::getStateCode));

        model.addAttribute("groupedTownships", grouped);
        model.addAttribute("nrcTypes", nrcService.getAllTypes());
        return "pre_enrollment";
    }

    @GetMapping({ "/login" })
    public String redirectToLogin(ModelMap model) {
        return "login";
    }

    @GetMapping({ "/auth/ping" })
    @ResponseBody
    public Map<String, String> redirectToIndex() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        return response;
    }

    // TEST---------------------
    // @PostMapping({"/"})
    // public String formHtml(@ModelAttribute PersonMock person, ModelMap model){
    // model.addAttribute("complete",true);
    // model.addAttribute("message","message arrived successfully");
    // model.addAttribute("person",person);
    // model.addAttribute("gg", "welcome");
    // return "index";
    // }
}
