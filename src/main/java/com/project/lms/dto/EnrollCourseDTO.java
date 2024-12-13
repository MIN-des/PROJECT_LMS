package com.project.lms.dto;

import com.project.lms.constant.RestStatus;
import lombok.Data;

@Data
public class EnrollCourseDTO {

  private Long courseId; // 강의 ID
  private String courseName; // 강의 이름
  private String studentId; // 학생 ID
  private int credits; // 학점
  private RestStatus status; // 잔여인원 상태
  private int restNum; // 잔여인원 숫자
  private String createdBy;

  public EnrollCourseDTO(Long courseId, String courseName, String studentId,
                         int credits, int restNum, RestStatus status,String createdBy) {
    this.courseId = courseId;
    this.courseName = courseName;
    this.studentId = studentId;
    this.credits = credits;
    this.restNum = restNum;
    this.status = status;
    this.createdBy = createdBy;
  }
}

