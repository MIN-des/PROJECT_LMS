package com.project.lms.service;

import com.project.lms.dto.EnrollDTO;
import com.project.lms.entity.Enroll;

public interface EnrollService {
  EnrollDTO addCourseToEnroll(String sId, Long cId);

  EnrollDTO convertToDTO(Enroll enroll);

  void removeCourseFromEnroll(String sId, Long cId);

  EnrollDTO createEnrollOnLogin(String sId);

}
