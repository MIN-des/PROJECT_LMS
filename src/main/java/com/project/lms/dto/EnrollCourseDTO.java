package com.project.lms.dto;

import com.project.lms.constant.Dept;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollCourseDTO {
  private Long ceId;
  private Long eId;
  private Long cId;
  private String cName;
  private int credits;
  private int maxCapacity;
  private int restNum;
  private String pId;
  private Dept dept;
  private String pName;
}

