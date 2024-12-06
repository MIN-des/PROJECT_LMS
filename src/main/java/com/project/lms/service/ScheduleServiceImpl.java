package com.project.lms.service;

import com.project.lms.dto.ScheduleDTO;
import com.project.lms.entity.Schedule;
import com.project.lms.repository.ScheduleRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

  private final ScheduleRepository scheduleRepository;

  @Override
  public Schedule createSchedule(ScheduleDTO scheduleDTO) {
    Schedule schedule = new Schedule();
    schedule.setContent(scheduleDTO.getContent());
    schedule.setStartDay(scheduleDTO.getStartDay());
    schedule.setEndDay(scheduleDTO.getEndDay());
    return scheduleRepository.save(schedule);
  }

  @Override
  public List<Schedule> getAllSchedules() {
    return scheduleRepository.findAll();
  }

  @Override
  public Schedule getScheduleById(Long sno) {
    return scheduleRepository.findById(sno)
            .orElseThrow(() -> new IllegalArgumentException("스케줄을 찾을 수 없습니다."));
  }

  @Override
  public void deleteSchedule(Long sno) {
    scheduleRepository.deleteById(sno);
  }

}
