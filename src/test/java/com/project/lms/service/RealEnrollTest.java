package com.project.lms.service;

import com.project.lms.dto.EnrollCourseDTO;
import com.project.lms.entity.Course;
import com.project.lms.entity.Student;
import com.project.lms.exception.OutOfRestNumException;
import com.project.lms.repository.CourseRepository;
import com.project.lms.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class RealEnrollTest {

  @Autowired
  private EnrollServiceImpl enrollService;

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private CourseRepository courseRepository;

  private String studentId = "S0000001"; // 실제 DB에 존재하는 studentId
  private Long courseId1 = 13L; // 실제 DB에 존재하는 courseId
  private Long courseId2 = 2L; // 실제 DB에 존재하는 courseId

  @BeforeEach
  void checkTestData() {
    // 실제 데이터베이스에 필요한 데이터가 있는지 확인
    Student student = studentRepository.findById(studentId).orElseThrow(() ->
            new IllegalStateException("Student not found in the database: " + studentId));

    Course course1 = courseRepository.findById(courseId1).orElseThrow(() ->
            new IllegalStateException("Course not found in the database: " + courseId1));

    Course course2 = courseRepository.findById(courseId2).orElseThrow(() ->
            new IllegalStateException("Course not found in the database: " + courseId2));

    assertThat(student).isNotNull();
    assertThat(course1).isNotNull();
    assertThat(course2).isNotNull();
  }

  /*@AfterEach
  void cleanup() {
    // 테스트 데이터 정리
    enrollService.removeAllEnrollments(studentId);
  }*/

  @Test
  void testAddCourseToEnroll() {
    // 장바구니에 강의 추가
    enrollService.addCourseToEnroll(studentId, courseId1);

    // 장바구니 확인
  //  List<EnrollCourseDTO> enrollCourses = enrollService.getEnrollCourses(studentId);

//    assertThat(enrollCourses).hasSize(1);
//    assertThat(enrollCourses.get(0).getCourseId()).isEqualTo(courseId1);
  }

  @Test
  void testAddDuplicateCourseToEnroll() {
    // 중복 강의 추가 시도
    enrollService.addCourseToEnroll(studentId, courseId1);

    // 중복 강의 추가 시 예외 발생 확인
    assertThrows(IllegalArgumentException.class, () -> enrollService.addCourseToEnroll(studentId, courseId1));
  }

  @Test
  void testRemoveCourseFromEnroll() {
    // 장바구니에 강의 추가
    enrollService.addCourseToEnroll(studentId, courseId1);

    // 강의 삭제
    enrollService.removeCourseFromEnroll(studentId, courseId1);

    // 장바구니 확인
//    List<EnrollCourseDTO> enrollCourses = enrollService.getEnrollCourses(studentId);
//    assertThat(enrollCourses).isEmpty();
  }

  @Test
  void testConfirmEnrollment() {
    // 강의 상태를 초기화
    Course course1 = courseRepository.findById(courseId1).orElseThrow();
    Course course2 = courseRepository.findById(courseId2).orElseThrow();
    course1.setRestNum(10);
    course2.setRestNum(10);
    courseRepository.save(course1);
    courseRepository.save(course2);
    // 강의 추가
    enrollService.addCourseToEnroll(studentId, courseId1);
    enrollService.addCourseToEnroll(studentId, courseId2);

    // 수강 신청 확정
    //enrollService.confirmEnrollment(studentId);

    // 장바구니 확인
//    List<EnrollCourseDTO> enrollCourses = enrollService.getEnrollCourses(studentId);
//    assertThat(enrollCourses).isEmpty();

    // 강의 잔여 인원 확인
    course1 = courseRepository.findById(courseId1).orElseThrow();
    course2 = courseRepository.findById(courseId2).orElseThrow();
    assertThat(course1.getRestNum()).isEqualTo(9); // 10에서 1 감소
    assertThat(course2.getRestNum()).isEqualTo(9); // 10에서 1 감소

  }

  @Test
  void testConfirmEnrollmentWithInsufficientSeats() {
    // 강의 추가
    enrollService.addCourseToEnroll(studentId, courseId2);

    // 강의 잔여 인원을 0으로 설정
    Course course2 = courseRepository.findById(courseId2).orElseThrow();
    course2.setRestNum(0);
    courseRepository.save(course2);

    // 수강 신청 확정 시 예외 발생 확인
    assertThrows(OutOfRestNumException.class, () -> enrollService.confirmEnrollment(studentId));
  }
}
