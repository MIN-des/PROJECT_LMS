package com.project.lms.controller;

import com.project.lms.dto.ScheduleDTO;
import com.project.lms.entity.Schedule;
import com.project.lms.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
  private final ScheduleService scheduleService;

  @GetMapping("/create")
  public String createScheduleForm(Model model) {
    model.addAttribute("schedule", new Schedule());
    return "schedule/create";
  }

  @PostMapping("/create")
  public String createSchedule(@ModelAttribute ScheduleDTO scheduleDTO) {
    scheduleService.createSchedule(scheduleDTO);
    return "redirect:/main";
  }

  @GetMapping("/modify/{sno}")
  public String editSchedule(@PathVariable Long sno, Model model) {
    Schedule schedule = scheduleService.getScheduleById(sno);
    model.addAttribute("schedule", schedule);
    return "schedule/modify";
  }

  @PostMapping("/modify")
  public String updateSchedule(@ModelAttribute ScheduleDTO scheduleDTO) {
    scheduleService.updateSchedule(scheduleDTO);
    return "redirect:/main";
  }

  @GetMapping("delete/{sno}")
  public String deleteSchedule(@PathVariable Long sno) {
    scheduleService.deleteSchedule(sno);
    return "redirect:/main";
  }

  @GetMapping("/list")
  public String getSchedules(@RequestParam(defaultValue = "0")int page,
                            @RequestParam(defaultValue = "5") int size,
                            Model model) {
    Page<Schedule> schedules = scheduleService.getSchedules(page,size);
      model.addAttribute("schedules", schedules.getContent());
      model.addAttribute("currentPage", schedules.getNumber());
      model.addAttribute("totalPages", schedules.getTotalPages());
      return "schedule/list";
  }
  @GetMapping("/calendar")
  public String getCalendarSchedules(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     Model model) {
    Page<Schedule> schedules = scheduleService.getSchedules(page, size);
    model.addAttribute("schedules", schedules.getContent()); // 동적 데이터 전달
    model.addAttribute("currentPage", schedules.getNumber());
    model.addAttribute("totalPages", schedules.getTotalPages());
    return "schedule/calendar"; // 수정된 HTML 파일 경로
  }


}
