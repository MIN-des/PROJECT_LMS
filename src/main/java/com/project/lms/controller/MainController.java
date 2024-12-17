package com.project.lms.controller;

import com.project.lms.config.CustomUserDetails;
import com.project.lms.entity.Board;
import com.project.lms.entity.Schedule;
import com.project.lms.service.ScheduleService;
import com.project.lms.service.admin.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class MainController {

  private final ScheduleService scheduleService;
  private final BoardServiceImpl boardService;

  @GetMapping("/main")  // /hello 경로로 접근 시 hello.html을 반환
  public String mainPage(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "5") int size,
                         Model model) {
    // 스케줄 서비스 호출 (ScheduleController와 협력)
    Page<Schedule> schedulePage = scheduleService.getSchedules(page, size);

    model.addAttribute("schedules", schedulePage.getContent());
    model.addAttribute("currentPage", schedulePage.getNumber());
    model.addAttribute("totalPages", schedulePage.getTotalPages());

    // 게시글 데이터 추가
    Page<Board> boardPage = boardService.getList(PageRequest.of(0, 5));
    model.addAttribute("boardList", boardPage.getContent());

    // 로그인 사용자 정보 추가
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
      model.addAttribute("userName", userDetails.getUsername());
    }
    // 날짜 리스트 생성 (오늘 날짜부터 7일)
    List<LocalDate> dateList = new ArrayList<>();
    LocalDate today = LocalDate.now();
    for (int i = 0; i <= 7; i++) {
      dateList.add(today.plusDays(i));
    }
    model.addAttribute("dateList", dateList); // 날짜 리스트 추가

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

    return "main :: #schedule-list"; // Fragment만 반환
  }

  // permit All
  @GetMapping("/dashboard/greetings")
  public String greetingsPage() {
    return "greetings_page";
  }

  @GetMapping("/dashboard")
  public String dashboardPage(HttpServletResponse response) {
    // 캐시 방지 헤더 추가
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies

    return "dashboard";
  }

  @GetMapping("/")
  public String root() {
    return "redirect:/login";
  }
}
