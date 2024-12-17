package com.project.lms.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EnrollDTO {
  private Long eId;
  private String studentId;
  private List<EnrollCourseDTO> enrollCourses;
}
