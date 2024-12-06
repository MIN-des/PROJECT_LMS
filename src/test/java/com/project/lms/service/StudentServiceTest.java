package com.project.lms.service;

import com.project.lms.constant.Dept;
import com.project.lms.constant.Gen;
import com.project.lms.constant.Role;
import com.project.lms.dto.StudentDTO;
import com.project.lms.entity.Student;
import com.project.lms.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//@Transactional
public class StudentServiceTest {
  @Autowired
  private StudentService studentService;
  @Autowired
  private StudentRepository studentRepository;

  @Test
  public void testCreateStudent() {
    // Given
    StudentDTO studentDTO = new StudentDTO(
            "S2404006",
            "일지매",
            "1234",
            "010-1742-9876",
            "서울시 강북구",
            "2000-08-29",
            "one@example.com",
            1,
            Gen.MALE,
            Dept.ARTS,
            Role.STUDENT
    );

    Student savedStudent = studentService.createStudent(studentDTO);

    assertNotNull(savedStudent);
//    assertEquals("S2401001", savedStudent.getSId());
//    assertEquals("홍길동", savedStudent.getSName());
  }

}
