package com.project.lms.service;

import com.project.lms.dto.EnrollCourseDTO;
import com.project.lms.entity.*;
import com.project.lms.repository.CourseRepository;
import com.project.lms.repository.EnrollRepository;
import com.project.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EnrollServiceImpl implements EnrollService {

  private final StudentRepository studentRepository;
  private final EnrollRepository enrollRepository;
  private final CourseRepository courseRepository;

  @Transactional
  public Enroll getOrCreateEnroll(String studentId) {
    Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

    Enroll enroll = student.getEnroll();
    if (enroll == null) {
      enroll = new Enroll();
      enroll.setStudent(student);
      student.setEnroll(enroll);
      enroll = enrollRepository.save(enroll); // 새로 생성된 경우에만 저장
    }

    return enroll;
  }

  @Transactional
  public void addCourseToEnroll(String studentId, Long courseId) {
    Enroll enroll = getOrCreateEnroll(studentId);

    Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다."));

    synchronized (enroll) { // 동기화 블록 추가 (멀티 스레딩 환경 대비)
      // 중복 강의 확인
      boolean isCourseAlreadyInEnroll = enroll.getEnrollCourses().stream()
              .anyMatch(item -> item.getCourse().getCId().equals(courseId));

      if (isCourseAlreadyInEnroll) {
        throw new IllegalArgumentException("이미 장바구니에 추가된 강의입니다.");
      }

      enroll.addCourse(course);
      enrollRepository.save(enroll);
    }
  }


  @Transactional(readOnly = true)
  public List<EnrollCourseDTO> getEnrollCourses(String studentId) {
    Enroll enroll = enrollRepository.findByStudent_sId(studentId)
            .orElseThrow(() -> new IllegalArgumentException("학생 ID :" + studentId + "장바구니를 찾을 수 없습니다."));

    return enroll.getEnrollCourses().stream()
            .map(item -> {
              Course course = item.getCourse();
              return new EnrollCourseDTO(
                      course.getCId(),
                      course.getCName(),
                      enroll.getStudent().getSId(),
                      course.getCredits(),
                      course.getRestNum(),
                      course.getStatus(),
                      course.getCreatedBy()
              );
            })
            .collect(Collectors.toList());
  }

  @Transactional
  public void removeCourseFromEnroll(String studentId, Long courseId) {
    Enroll enroll = getOrCreateEnroll(studentId);

    boolean removed = enroll.getEnrollCourses().removeIf(item -> item.getCourse().getCId().equals(courseId));
    if (!removed) {
      throw new IllegalArgumentException("장바구니에 강의 ID: " + courseId + "가 존재하지 않습니다.");
    }

    enrollRepository.save(enroll);
  }

  @Transactional
  public void confirmEnrollment(String studentId) {
    Enroll enroll = enrollRepository.findByStudent_sId(studentId)
            .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다."));

    if (enroll.getEnrollCourses().isEmpty()) {
      throw new IllegalStateException("장바구니에 담긴 강의가 없습니다.");
    }

    for (EnrollCourse enrollCourse : enroll.getEnrollCourses()) {
      Course course = enrollCourse.getCourse();

      // 잔여 정원 확인
      if (course.getRestNum() <= 0) {
        throw new IllegalStateException("강의 [" + course.getCName() + "]의 잔여 정원이 부족합니다.");
      }

      course.removeStock(1); // 잔여 인원 감소
      courseRepository.save(course);
    }

    enroll.getEnrollCourses().clear(); // 장바구니 비우기
    enrollRepository.save(enroll);
  }

}
