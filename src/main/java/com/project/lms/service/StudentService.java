package com.project.lms.service;

import com.project.lms.dto.StudentDTO;
import com.project.lms.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface StudentService {
	StudentDTO createStudent(StudentDTO studentDTO);

	Optional<StudentDTO> getStudentById(String sId);

	StudentDTO updateStudent(String sId, StudentDTO studentDTO);

	void deleteStudent(String sId);

	Page<StudentDTO> searchStudentsById(String sId, Pageable pageable);

	Page<StudentDTO> searchStudentsByName(String sName, Pageable pageable);

	Page<StudentDTO> searchStudentsByDept(String sDept, Pageable pageable);

	Page<StudentDTO> getAllStudents(Pageable pageable);

	Student getStudentInfo(String sId); // 학생 정보 조회

	Student updateInfo(String sId, StudentDTO updateInfoDTO);
}
