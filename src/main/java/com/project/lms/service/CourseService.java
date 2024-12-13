package com.project.lms.service;

import com.project.lms.dto.CourseDTO;
import com.project.lms.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> getCoursesByProfessorId(String pId);

    CourseDTO createCourse(CourseDTO courseDTO);

    // 강의 상세 정보 조회
    // Optional: Null 값도 불러옴
    Optional<CourseDTO> getCourseById(Long cId);

    // 강의 정보 업데이트
    void updateCourse(Long cId, CourseDTO courseDTO);

    Page<CourseDTO> searchCoursesById(Long cId, Pageable pageable);

    Page<CourseDTO> searchCourseByProfessor_pId(String pId, Pageable pageable);

    Page<CourseDTO> searchCoursesByName(String cName, Pageable pageable);

    Page<CourseDTO> searchCourseByCreatedBy(String createdBy, Pageable pageable);

    Page<CourseDTO> getAllCourses(Pageable pageable);
}
