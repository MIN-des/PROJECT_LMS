package com.project.lms.service;

import com.project.lms.dto.ScheduleDTO;
import com.project.lms.entity.Schedule;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ScheduleService {
  Schedule createSchedule(ScheduleDTO scheduleDTO);

  List<Schedule> getAllSchedules();

  Schedule getScheduleById(Long sno);

  void deleteSchedule(Long sno);

  void updateSchedule(ScheduleDTO scheduleDTO);

  Page<Schedule> getSchedules(int page, int size);
}
