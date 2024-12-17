package com.project.lms.service;

import com.project.lms.dto.EnrollDTO;
import com.project.lms.entity.Course;
import com.project.lms.entity.Enroll;
import com.project.lms.entity.Student;
import com.project.lms.repository.CourseRepository;
import com.project.lms.repository.EnrollRepository;
import com.project.lms.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
//@Transactional
public class EnrollServiceTest {

  @Autowired
  private EnrollServiceImpl enrollService;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private EnrollRepository enrollRepository;

  private Student testStudent;
  private Course testCourse;

  @BeforeEach
  void setup() {
    // 테스트 데이터 준비
    testStudent = studentRepository.findById("S0000001")
            .orElseThrow(() -> new IllegalStateException("테스트 학생 데이터가 존재하지 않습니다."));

    testCourse = courseRepository.findById(1L)
            .orElseThrow(() -> new IllegalStateException("테스트 강의 데이터가 존재하지 않습니다."));
  }

  @Test
  void testCreateEnrollOnLogin() {
    // 로그인 시 장바구니 생성 또는 조회
    EnrollDTO enrollDTO = enrollService.createEnrollOnLogin(testStudent.getSId());

    // 결과 검증
    assertThat(enrollDTO).isNotNull();
    assertThat(enrollDTO.getStudentId()).isEqualTo(testStudent.getSId());

    Optional<Enroll> enrollOptional = enrollRepository.findByStudent(testStudent);
    assertThat(enrollOptional).isPresent();
    assertThat(enrollOptional.get().getStudent()).isEqualTo(testStudent);
  }

  @Test
  void testAddCourseToEnroll() {
    // 장바구니에 강의 추가
    EnrollDTO enrollDTO = enrollService.addCourseToEnroll(testStudent.getSId(), testCourse.getCId());

    // 결과 검증
    assertThat(enrollDTO.getEnrollCourses()).isNotEmpty();
    assertThat(enrollDTO.getEnrollCourses().get(0).getCId()).isEqualTo(testCourse.getCId());

    Enroll enroll = enrollRepository.findByStudent(testStudent).orElseThrow();
    assertThat(enroll.getEnrollCourses()).hasSize(1);
    assertThat(enroll.getEnrollCourses().get(0).getCourse()).isEqualTo(testCourse);
  }

  @Test
  void testAddCourseToEnroll_DuplicateCourse() {
    // 장바구니에 강의 추가
    enrollService.addCourseToEnroll(testStudent.getSId(), testCourse.getCId());

    // 동일한 강의를 추가하려고 할 때 예외 발생
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      enrollService.addCourseToEnroll(testStudent.getSId(), testCourse.getCId());
    });

    assertThat(exception.getMessage()).isEqualTo("이미 장바구니에 추가된 강의입니다.");
  }

  @Test
  void testRemoveCourseFromEnroll() {
    // 장바구니에 강의 추가
    enrollService.addCourseToEnroll(testStudent.getSId(), testCourse.getCId());

    // 장바구니에서 강의 제거
    enrollService.removeCourseFromEnroll(testStudent.getSId(), testCourse.getCId());

    // 결과 검증
    Enroll enroll = enrollRepository.findByStudent(testStudent).orElseThrow();
    assertThat(enroll.getEnrollCourses()).isEmpty();
  }

  @Test
  void testRemoveCourseFromEnroll_CourseNotFound() {
    // 장바구니에 없는 강의를 제거하려고 할 때 예외 발생
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      enrollService.removeCourseFromEnroll(testStudent.getSId(), testCourse.getCId());
    });

    assertThat(exception.getMessage()).isEqualTo("장바구니에 해당 강의가 없습니다.");
  }
}
