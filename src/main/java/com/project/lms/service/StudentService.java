package com.project.lms.service;

import com.project.lms.dto.StudentDTO;
import com.project.lms.entity.Student;

import java.util.Map;

public interface StudentService {

  Student getStudentInfo(String sId); // 학생 정보 조회

  Student updateInfo(String sId, StudentDTO updateInfoDTO);

  Map<String, Object> getOrdersWithScores(String sId);
}
