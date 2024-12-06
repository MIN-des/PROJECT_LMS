package com.project.lms.controller;

import com.project.lms.dto.ScheduleDTO;
import com.project.lms.dto.StudentDTO;
import com.project.lms.entity.Schedule;
import com.project.lms.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
  private final ScheduleService scheduleService;

  @GetMapping("/list")
  public String scheduleList(Model model) {
    List<Schedule> schedules = scheduleService.getAllSchedules();
    model.addAttribute("schedules", schedules);
    return "schedule/list";
  }

  @GetMapping("/create")
  public String createScheduleForm(Model model) {
    model.addAttribute("schedule", new ScheduleDTO());
    return "schedule/create";
  }

  @PostMapping("/create")
  public String createSchedule(@ModelAttribute ScheduleDTO scheduleDTO) {
    scheduleService.createSchedule(scheduleDTO);
    return "redirect:/schedule/list";
  }

  @PostMapping("delete/{sno}")
  public String deleteSchedule(@PathVariable Long sno) {
    scheduleService.deleteSchedule(sno);
    return "redirect:/schedule/list";
  }
}
