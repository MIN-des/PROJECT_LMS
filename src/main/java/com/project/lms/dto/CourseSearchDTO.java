package com.project.lms.dto;

import com.project.lms.constant.RestStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseSearchDTO { // 강의 검색

  private String searchDateType; // 검색 유형
  private RestStatus searchRestStatus; // 잔여 여부
  private String searchBy;
  private String searchQuery = ""; // 검색 조건
}
