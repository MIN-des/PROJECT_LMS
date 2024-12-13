package com.project.lms.service;

import com.project.lms.dto.EnrollCourseDTO;
import com.project.lms.entity.Enroll;

import java.util.List;

public interface EnrollService {
  Enroll getOrCreateEnroll(String studentId);
  void addCourseToEnroll(String studentId, Long courseId);
//  List<EnrollCourseDTO> getEnrollCourses(String studentId);
  void removeCourseFromEnroll(String studentId, Long courseId);
  void confirmEnrollment(String studentId);
}
