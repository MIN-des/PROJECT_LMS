package com.project.lms.service;

import com.project.lms.dto.CourseDTO;
import com.project.lms.entity.Course;
import com.project.lms.entity.Professor;
import com.project.lms.repository.CourseRepository;
import com.project.lms.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

  private final ProfessorRepository professorRepository;
  private final CourseRepository courseRepository;
  private final ModelMapper modelMapper;

  @Override
  public Page<CourseDTO> getMyCourses(String pId, Pageable pageable) {
    return courseRepository.findByProfessor_pId(pId, pageable)
            .map(course -> {
              CourseDTO courseDTO = CourseDTO.of(course);
              courseDTO.setPId(course.getProfessor().getPId());
              courseDTO.setPDept(course.getProfessor().getPDept());
              courseDTO.setCName(course.getCName());
              courseDTO.setCredits(course.getCredits());
              courseDTO.setMaxCapacity(course.getMaxCapacity());
              courseDTO.setRestNum(course.getRestNum());

              return courseDTO;
            });
  }

  @Override
  public Page<CourseDTO> searchMyCoursesById(String pId, Long cId, Pageable pageable) {
    return courseRepository.findCoursesByProfessorAndId(pId, cId, pageable)
            .map(course -> {
              CourseDTO courseDTO = CourseDTO.of(course);
              courseDTO.setPId(course.getProfessor().getPId());
              courseDTO.setPDept(course.getProfessor().getPDept());
              courseDTO.setCName(course.getCName());
              courseDTO.setCredits(course.getCredits());
              courseDTO.setMaxCapacity(course.getMaxCapacity());
              courseDTO.setRestNum(course.getRestNum());

              return courseDTO;
            });
  }

  @Override
  public Page<CourseDTO> searchMyCoursesByName(String pId, String cName, Pageable pageable) {
    return courseRepository.findByProfessor_PIdAndCNameContainingIgnoreCase(
                    pId, cName, pageable)
            .map(course -> {
              CourseDTO courseDTO = CourseDTO.of(course);
              courseDTO.setPId(course.getProfessor().getPId());
              courseDTO.setPDept(course.getProfessor().getPDept());
              courseDTO.setCName(course.getCName());
              courseDTO.setCredits(course.getCredits());
              courseDTO.setMaxCapacity(course.getMaxCapacity());
              courseDTO.setRestNum(course.getRestNum());

              return courseDTO;
            });
  }

  // 강의 생성 메소드
  @Override
  public CourseDTO createCourse(CourseDTO courseDTO) {
    Course course = modelMapper.map(courseDTO, Course.class);

    // pId를 통해 Professor 엔티티 조회
    Professor professor = professorRepository.findById(courseDTO.getPId())
            .orElseThrow(() -> new EntityNotFoundException("해당 ID의 교수를 찾을 수 없습니다."));

    course.setProfessor(professor); // 강의에 교수 설정

    return CourseDTO.of(courseRepository.save(course));
  }

  @Override
  public Optional<CourseDTO> getCourseById(Long cId) {
    return courseRepository.findById(cId)
            .map(course -> {
              CourseDTO courseDTO = new CourseDTO();
              courseDTO.setCId(course.getCId());
              courseDTO.setCName(course.getCName());
              courseDTO.setCredits(course.getCredits());
              courseDTO.setMaxCapacity(course.getMaxCapacity());
              courseDTO.setRestNum(course.getRestNum());
              courseDTO.setPId(course.getProfessor().getPId());
              courseDTO.setPDept(course.getProfessor().getPDept());
              courseDTO.setPName(course.getProfessor().getPName());

              return courseDTO;
            });
  }

  @Override
  public void updateCourse(Long cId, CourseDTO courseDTO) {
    // 현재 로그인한 계정 정보 가져오기
    String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

    // 해당 강의 조회
    Course course = courseRepository.findById(cId)
            .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + cId));

    // 강의를 등록한 교수와 현재 로그인한 사용자가 동일한지 확인
    if (!course.getProfessor().getPId().equals(currentUser)) {
      throw new AccessDeniedException("You do not have permission to update this course.");
    }

    course.updateCourse(courseDTO);
    courseRepository.save(course);
  }


  @Override
  public Page<CourseDTO> searchCoursesById(Long cId, Pageable pageable) {
    return courseRepository.findBycId(cId, pageable)
            .map(course -> {
              CourseDTO courseDTO = modelMapper.map(course, CourseDTO.class);
              courseDTO.setPId(course.getProfessor().getPId()); // 수동 매핑
              courseDTO.setPDept(course.getProfessor().getPDept());
              courseDTO.setPName(course.getProfessor().getPName());

              return courseDTO;
            });
  }

  @Override
  public Page<CourseDTO> searchCoursesByName(String cName, Pageable pageable) {
    return courseRepository.findBycNameContainingIgnoreCase(cName, pageable)
            .map(course -> {
              CourseDTO courseDTO = modelMapper.map(course, CourseDTO.class);
              courseDTO.setPId(course.getProfessor().getPId()); // 수동 매핑
              courseDTO.setPDept(course.getProfessor().getPDept());
              courseDTO.setPName(course.getProfessor().getPName());

              return courseDTO;
            });
  }

  // 교수 검색 메소드
  @Override
  public Page<CourseDTO> searchCourseByProfessor_pId(String pId, Pageable pageable) {
    return courseRepository.findByProfessor_pIdContainingIgnoreCase(pId, pageable)
            .map(course -> {
              CourseDTO courseDTO = modelMapper.map(course, CourseDTO.class);
              courseDTO.setPId(course.getProfessor().getPId()); // 수동 매핑
              courseDTO.setPDept(course.getProfessor().getPDept());
              courseDTO.setPName(course.getProfessor().getPName());

              return courseDTO;
            });
  }

  @Override
  public Page<CourseDTO> searchCourseByProfessor_pName(String pName, Pageable pageable) {
    return courseRepository.findByProfessor_pNameContainingIgnoreCase(pName, pageable)
            .map(course -> {
              CourseDTO courseDTO = modelMapper.map(course, CourseDTO.class);
              courseDTO.setPId(course.getProfessor().getPId()); // 수동 매핑
              courseDTO.setPDept(course.getProfessor().getPDept());
              courseDTO.setPName(course.getProfessor().getPName());

              return courseDTO;
            });
  }

  // 강의 조회 메소드
  @Override
  public Page<CourseDTO> getAllCourses(Pageable pageable) {
    return courseRepository.findAll(pageable)
            .map(course -> {
              CourseDTO courseDTO = CourseDTO.of(course);
              courseDTO.setPId(course.getProfessor().getPId());
              courseDTO.setPDept(course.getProfessor().getPDept());
              courseDTO.setPName(course.getProfessor().getPName());

              return courseDTO;
            });
  }
}