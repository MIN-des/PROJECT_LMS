package com.project.lms.service;

import com.project.lms.dto.StudentDTO;
import com.project.lms.entity.Student;
import com.project.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

  private final StudentRepository studentRepository;

  @Override
  public Student createStudent(StudentDTO studentDTO) {
    // DTO를 엔티티로 변환
    Student student = new Student();
    student.setSId(studentDTO.getSId());
    student.setSName(studentDTO.getSName());
    student.setSPw(studentDTO.getSPw());
    student.setSTel(studentDTO.getSTel());
    student.setSAdd(studentDTO.getSAdd());
    student.setSBirth(studentDTO.getSBirth());
    student.setSEmail(studentDTO.getSEmail());
    student.setGrade(studentDTO.getGrade());
    student.setSGen(studentDTO.getSGen());
    student.setSDept(studentDTO.getSDept());
    student.setRole(studentDTO.getRole());

    // 저장 후 반환
    return studentRepository.save(student);
  }

  @Override
  public Student getStudentInfo(String sId) {
    return studentRepository.findById(sId)
            .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));
  }

  @Override
  public Student updateInfo(String sId, StudentDTO updateInfoDTO) {
    Student updateInfo = studentRepository.findById(sId)
            .orElseThrow(() -> new IllegalArgumentException("학생 정보를 찾을 수 없습니다."));

    updateInfo.setSName(updateInfoDTO.getSName() != null ? updateInfoDTO.getSName() : updateInfo.getSName());
    updateInfo.setSPw(updateInfoDTO.getSPw() != null ? updateInfoDTO.getSPw() : updateInfo.getSPw());
    updateInfo.setSTel(updateInfoDTO.getSTel() != null ? updateInfoDTO.getSTel() : updateInfo.getSTel());
    updateInfo.setSAdd(updateInfoDTO.getSAdd() != null ? updateInfoDTO.getSAdd() : updateInfo.getSAdd());
    updateInfo.setSBirth(updateInfoDTO.getSBirth() != null ? updateInfoDTO.getSBirth() : updateInfo.getSBirth());
    updateInfo.setSEmail(updateInfoDTO.getSEmail() != null ? updateInfoDTO.getSEmail() : updateInfo.getSEmail());
    updateInfo.setGrade(updateInfoDTO.getGrade() > 0 ? updateInfoDTO.getGrade() : updateInfo.getGrade());
    updateInfo.setSDept(updateInfoDTO.getSDept() != null ? updateInfoDTO.getSDept() : updateInfo.getSDept());
    updateInfo.setSGen(updateInfoDTO.getSGen() != null ? updateInfoDTO.getSGen() : updateInfo.getSGen());
    updateInfo.setRole(updateInfoDTO.getRole() != null ? updateInfoDTO.getRole() : updateInfo.getRole());

    return studentRepository.save(updateInfo);
  }

//  private StudentDTO entityToDTO(Student student) {
//    // 엔티티 -> DTO 변환 메서드
//    return new StudentDTO(
//            student.getSId(),
//            student.getSName(),
//            student.getSPw(),
//            student.getSTel(),
//            student.getSAdd(),
//            student.getSBirth(),
//            student.getSEmail(),
//            student.getGrade(),
//            student.getSGen(),
//            student.getSDept(),
//            student.getRole()
//    );
//  }
}
