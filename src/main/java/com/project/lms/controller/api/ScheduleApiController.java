package com.project.lms.controller.api;

import com.project.lms.entity.Schedule;
import com.project.lms.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class ScheduleApiController {
  private final ScheduleService scheduleService;

  @GetMapping
  public List<Schedule> getSchedules() {
    return scheduleService.getAllSchedules();
  }
}
