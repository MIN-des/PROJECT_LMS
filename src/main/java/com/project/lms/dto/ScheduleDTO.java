package com.project.lms.dto;

import com.project.lms.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
  private Long sno;
  private String content;
  private String startDay;
  private String endDay;
}
