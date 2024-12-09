package com.project.lms.service;

import com.project.lms.constant.RestStatus;
import com.project.lms.dto.CourseDTO;
import com.project.lms.entity.Course;
import com.project.lms.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    // 강의 생성 메소드
    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {

        // 강의 이름 중복 검사
        if (courseRepository.existsBycName(courseDTO.getCName())) {
            throw new IllegalArgumentException("이미 중복된 강의 이름이 존재합니다.");
        }

        Course course = modelMapper.map(courseDTO, Course.class);

        return modelMapper.map(courseRepository.save(course), CourseDTO.class);
    }

    @Override
    public Optional<CourseDTO> getCourseById(Long cId) {
        return courseRepository.findById(cId)
                .map(course -> modelMapper.map(course, CourseDTO.class));
    }

    // 강의 업데이트 메소드
    @Override
    public void updateCourse(Long cId, CourseDTO courseDTO) {
        // 현재 로그인한 계정 정보 가져옴
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        Course course = courseRepository.findById(cId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + cId));

        // 현재 로그인한 계정이 아닌 경우 접근 제한 메시지 출력
        if (!course.getCreatedBy().equals(currentUser)) {
            throw new AccessDeniedException("You do not have permission to update this course.");
        }

        // 업데이트 로직
        course.updateCourse(courseDTO);
        courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long cId) {
        Course course = courseRepository.findById(cId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + cId));

        // 강의 삭제
        courseRepository.deleteById(cId);
    }

    @Override
    public String findCreatedBy(Long cId) {
        return courseRepository.findCreatedBy(cId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + cId));
    }

    @Override
    public Page<CourseDTO> searchCoursesById(Long cId, Pageable pageable) {
        return courseRepository.findBycId(cId, pageable)
                .map(course -> modelMapper.map(course, CourseDTO.class));
    }

    @Override
    public Page<CourseDTO> searchCoursesByName(String cName, Pageable pageable) {
        return courseRepository.findBycNameContainingIgnoreCase(cName, pageable)
                .map(course -> modelMapper.map(course, CourseDTO.class));
    }

    @Override
    public Page<CourseDTO> searchCourseByCreatedBy(String createdBy, Pageable pageable) {
        return courseRepository.findByCreatedByContainingIgnoreCase(createdBy, pageable)
                .map(course -> modelMapper.map(course, CourseDTO.class));
    }

    @Override
    public Page<CourseDTO> searchCoursesByRestStatus(String status, Pageable pageable) {
        try {
            RestStatus rest = RestStatus.valueOf(status.toUpperCase());

            return courseRepository.findByStatus(rest, pageable)
                    .map(course -> modelMapper.map(course, CourseDTO.class));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 상태 값 입니다.");
        }
    }

    // 강의 조회 메소드
    @Override
    public Page<CourseDTO> getAllCourses(Pageable pageable) {
        Page<Course> courses = courseRepository.findAll(pageable);

        courses.forEach(course -> System.out.println(course)); // 디버깅

        return courseRepository.findAll(pageable)
                .map(course -> modelMapper.map(course, CourseDTO.class));
    }
}