package com.project.lms.service;

import com.project.lms.dto.CourseDTO;
import com.project.lms.dto.EnrollDTO;
import com.project.lms.entity.Enroll;

import java.util.List;
import java.util.Map;

public interface EnrollService {
  EnrollDTO addCourseToEnroll(String sId, Long cId);
  EnrollDTO convertToDTO(Enroll enroll);
  void removeCourseFromEnroll(String sId, Long cId);
  EnrollDTO createEnrollOnLogin(String sId);
//  Map<String, Object> getOrdersWithScores(String sId);
}
