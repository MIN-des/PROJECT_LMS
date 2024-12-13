package com.project.lms.repository;

import com.project.lms.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

  List<Course> findCoursesByProfessor_pId(String pId);

  // 모든 강의 목록 조회
  Page<Course> findAll(Pageable pageable);

  // 강의 ID로 검색하는 경우, 한 글자만 포함되어도 페이징 처리되어 조회
  Page<Course> findBycId(Long cId, Pageable pageable);

  // 강의명으로 검색하는 경우, 한 글자만 포함되어도 페이징 처리되어 조회
  Page<Course> findBycNameContainingIgnoreCase(String cName, Pageable pageable);

  Page<Course> findByProfessor_pIdContainingIgnoreCase(String pId, Pageable pageable);
}