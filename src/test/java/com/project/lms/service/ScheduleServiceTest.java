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
    ScheduleDTO scheduleDTO = new ScheduleDTO();
//    ScheduleDTO scheduleDTO = new ScheduleDTO(
//            4L,
//            "학적기재자료 입력기간",
//            "2025-01-06",
//            "2025-01-20"
//    );
    for(int i = 1; i<10; i++) {
      scheduleDTO.setContent("스케줄 페이징 테스트2" + i);
      scheduleDTO.setStartDay("2024-02-2" + i);
      scheduleDTO.setEndDay("2024-03-2" + i);
      Schedule savedSchedule = scheduleService.createSchedule(scheduleDTO);
      assertNotNull(scheduleDTO);
    }
  }
}
