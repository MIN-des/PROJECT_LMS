package com.project.lms.service;

import com.project.lms.dto.ProfessorDTO;
import com.project.lms.dto.StudentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AdminService {
  // 교수 관련 메서드
  ProfessorDTO createProfessor(ProfessorDTO professorDTO);

  Optional<ProfessorDTO> getProfessorById(String pId);

  ProfessorDTO updateProfessor(String pId, ProfessorDTO professorDTO);

  void deleteProfessor(String pId);

  Page<ProfessorDTO> searchProfessorsById(String pId, Pageable pageable);

  Page<ProfessorDTO> searchProfessorsByName(String pName, Pageable pageable);

  Page<ProfessorDTO> searchProfessorsByDept(String pDept, Pageable pageable);

  Page<ProfessorDTO> getAllProfessors(Pageable pageable);

  // 학생 관련 메서드
  StudentDTO createStudent(StudentDTO studentDTO);

  Optional<StudentDTO> getStudentById(String sId);

  StudentDTO updateStudent(String sId, StudentDTO studentDTO);

  void deleteStudent(String sId);

  Page<StudentDTO> searchStudentsById(String sId, Pageable pageable);

  Page<StudentDTO> searchStudentsByName(String sName, Pageable pageable);

  Page<StudentDTO> searchStudentsByDept(String sDept, Pageable pageable);

  Page<StudentDTO> getAllStudents(Pageable pageable);
}
