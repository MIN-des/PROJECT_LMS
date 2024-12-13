package com.project.lms.controller;

import com.project.lms.config.CustomUserDetails;
import com.project.lms.entity.Schedule;
import com.project.lms.repository.AdminRepository;
import com.project.lms.repository.ProfessorRepository;
import com.project.lms.repository.StudentRepository;
import com.project.lms.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ScheduleService scheduleService;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    @GetMapping("/main")  // /hello 경로로 접근 시 hello.html을 반환
    public String mainPage(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "5") int size,
                           Model model) {
        // 스케줄 서비스 호출 (ScheduleController와 협력)
        Page<Schedule> schedulePage = scheduleService.getSchedules(page, size);

        model.addAttribute("schedules", schedulePage.getContent());
        model.addAttribute("currentPage", schedulePage.getNumber());
        model.addAttribute("totalPages", schedulePage.getTotalPages());

        // 로그인 사용자 정보 추가
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//            model.addAttribute("userId", userDetails.getId());
            model.addAttribute("userName", userDetails.getUsername());
        }
        return "main";  // main.html 파일을 찾아 반환
    }

    // AJAX 요청을 처리하고 Fragment만 반환
    @GetMapping("/main/schedules")
    public String scheduleListFragment(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size,
                                       Model model) {
        Page<Schedule> schedulePage = scheduleService.getSchedules(page, size);

        model.addAttribute("schedules", schedulePage.getContent());
        model.addAttribute("currentPage", schedulePage.getNumber());
        model.addAttribute("totalPages", schedulePage.getTotalPages());

        return "main :: #schedule-list-container"; // Fragment만 반환
    }

    // permit All
    @GetMapping("/dashboard/greetings")
    public String greetingsPage() {
        return "greetings_page";
    }

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }
}
