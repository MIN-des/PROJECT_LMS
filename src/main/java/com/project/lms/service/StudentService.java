package com.project.lms.service;

import com.project.lms.dto.StudentDTO;
import com.project.lms.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;
import java.util.Optional;

public interface StudentService {

	Student getStudentInfo(String sId); // 학생 정보 조회

	Student updateInfo(String sId, StudentDTO updateInfoDTO);
  Map<String, Object> getOrdersWithScores(String sId);
}
