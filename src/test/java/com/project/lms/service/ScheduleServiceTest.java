package com.project.lms.service;

import com.project.lms.dto.ScheduleDTO;
import com.project.lms.entity.Schedule;
import com.project.lms.repository.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootTest
public class ScheduleServiceTest {
  @Autowired
  private ScheduleService scheduleService;
  @Autowired
  private ScheduleRepository scheduleRepository;

  @Test
  void createSchedule() {
    ScheduleDTO scheduleDTO = new ScheduleDTO(
            4L,
            "등록금 납부",
            "2024-01-06",
            "2024-01-13"
    );

    Schedule savedSchedule = scheduleService.createSchedule(scheduleDTO);

    assertNotNull(scheduleDTO);
  }
}
