package com.project.lms.service;

import com.project.lms.dto.EnrollDTO;
import com.project.lms.dto.EnrollCourseDTO;
import com.project.lms.entity.*;
import com.project.lms.repository.CourseRepository;
import com.project.lms.repository.EnrollRepository;
import com.project.lms.repository.OrderRepository;
import com.project.lms.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnrollServiceImpl implements EnrollService {

  private final EnrollRepository enrollRepository;
  private final StudentRepository studentRepository;
  private final CourseRepository courseRepository;
  private final OrderRepository orderRepository;

  public EnrollServiceImpl(EnrollRepository enrollRepository, StudentRepository studentRepository, CourseRepository courseRepository, OrderRepository orderRepository) {
    this.enrollRepository = enrollRepository;
    this.studentRepository = studentRepository;
    this.courseRepository = courseRepository;
    this.orderRepository = orderRepository;
  }

  // 공통 로직 추출: 학생 및 장바구니 조회
  private Enroll findOrCreateEnroll(String sId) {
    Student student = studentRepository.findById(sId)
            .orElseThrow(() -> new IllegalArgumentException("학생 정보를 찾을 수 없습니다."));

    return enrollRepository.findByStudent(student)
            .orElseGet(() -> {
              Enroll newEnroll = new Enroll();
              newEnroll.setStudent(student);
              return enrollRepository.save(newEnroll);
            });
  }

  public EnrollDTO createEnrollOnLogin(String sId) {
    Enroll enroll = findOrCreateEnroll(sId);
    return convertToDTO(enroll);
  }

  public EnrollDTO addCourseToEnroll(String sId, Long cId) {
    Enroll enroll = findOrCreateEnroll(sId);

    Course course = courseRepository.findById(cId)
            .orElseThrow(() -> new IllegalArgumentException("강의 정보를 찾을 수 없습니다."));

    // 중복 확인
    boolean isAlreadyAdded = enroll.getEnrollCourses().stream()
            .anyMatch(enrollCourse -> enrollCourse.getCourse().getCId().equals(cId));

    if (isAlreadyAdded) {
      throw new IllegalArgumentException("이미 추가된 강의입니다.");
    }

    // 강의 추가
    EnrollCourse enrollCourse = new EnrollCourse();
    enrollCourse.setEnroll(enroll);
    enrollCourse.setCourse(course);
    enroll.getEnrollCourses().add(enrollCourse);

    enrollRepository.save(enroll);
    return convertToDTO(enroll);
  }

  public EnrollDTO convertToDTO(Enroll enroll) {
    EnrollDTO enrollDTO = new EnrollDTO();
    enrollDTO.setEId(enroll.getEId());
    enrollDTO.setStudentId(enroll.getStudent().getSId());

    // Lazy 로딩 방지: DTO 변환 전에 필요한 데이터만 로드
    List<EnrollCourseDTO> courseDTOs = enroll.getEnrollCourses().stream()
            .map(enrollCourse -> {
              Course course = enrollCourse.getCourse(); // Lazy 로딩 발생 가능
              EnrollCourseDTO dto = new EnrollCourseDTO();
              dto.setCeId(enrollCourse.getCeId());
              dto.setEId(enroll.getEId());
              dto.setCId(course.getCId());
              dto.setCName(course.getCName());
              dto.setCredits(course.getCredits());
              dto.setMaxCapacity(course.getMaxCapacity());
              dto.setRestNum(course.getRestNum());
              dto.setPId(course.getCreatedBy());
              dto.setDept(course.getProfessor().getPDept());
              dto.setPName(course.getProfessor().getPName());
              return dto;
            })
            .collect(Collectors.toList());

    enrollDTO.setEnrollCourses(courseDTOs);
    return enrollDTO;
  }


  public void removeCourseFromEnroll(String sId, Long cId) {
    Enroll enroll = findOrCreateEnroll(sId);

    // 강의 제거
    EnrollCourse enrollCourse = enroll.getEnrollCourses().stream()
            .filter(ec -> ec.getCourse().getCId().equals(cId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("장바구니에 해당 강의가 없습니다."));

    enroll.getEnrollCourses().remove(enrollCourse);

    enrollRepository.save(enroll);
  }
}
