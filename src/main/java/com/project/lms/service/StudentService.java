package com.project.lms.service;

import com.project.lms.dto.StudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
}
