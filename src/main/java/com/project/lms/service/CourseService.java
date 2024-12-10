package com.project.lms.service;

import com.project.lms.dto.CourseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CourseService {

    CourseDTO createCourse(CourseDTO courseDTO);

    // 강의 상세 정보 조회
    // Optional: Null 값도 불러옴
    Optional<CourseDTO> getCourseById(Long cId);

    // 강의 정보 업데이트
    void updateCourse(Long cId, CourseDTO courseDTO);

    // 강의 삭제
    void deleteCourse(Long cId);

    // createdBy로 등록한 사람만 본인 강의 삭제할 수 있게 찾는 메소드
    String findCreatedBy(Long cId);

    Page<CourseDTO> searchCoursesById(Long cId, Pageable pageable);

    Page<CourseDTO> searchCoursesByName(String cName, Pageable pageable);

    Page<CourseDTO> searchCourseByCreatedBy(String createdBy, Pageable pageable);

    Page<CourseDTO> searchCoursesByRestStatus(String restStatus, Pageable pageable);

    Page<CourseDTO> getAllCourses(Pageable pageable);
}
