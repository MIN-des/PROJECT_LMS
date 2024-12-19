package com.project.lms.repository;

import com.project.lms.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {

  // 특정 교수의 모든 강의 조회 (페이징)
  Page<Course> findByProfessor_pId(String pId, Pageable pageable);

  // 특정 교수의 강의 ID로 검색
  @Query("SELECT c FROM Course c WHERE c.professor.pId = :pId AND c.cId = :cId")
  Page<Course> findCoursesByProfessorAndId(@Param("pId") String pId, @Param("cId") Long cId, Pageable pageable);

  // 특정 교수의 강의 이름으로 검색
  @Query("SELECT c FROM Course c WHERE c.professor.pId = :pId AND LOWER(c.cName) LIKE LOWER(CONCAT('%', :cName, '%'))")
  Page<Course> findByProfessor_PIdAndCNameContainingIgnoreCase(
          @Param("pId") String pId, @Param("cName") String cName, Pageable pageable);

  // 모든 강의 목록 조회
  Page<Course> findAll(Pageable pageable);

  // 강의 ID로 검색하는 경우, 한 글자만 포함되어도 페이징 처리되어 조회
  Page<Course> findBycId(Long cId, Pageable pageable);

  // 강의명으로 검색하는 경우, 한 글자만 포함되어도 페이징 처리되어 조회
  Page<Course> findBycNameContainingIgnoreCase(String cName, Pageable pageable);

  Page<Course> findByProfessor_pIdContainingIgnoreCase(String pId, Pageable pageable);

  Page<Course> findByProfessor_pNameContainingIgnoreCase(String pName, Pageable pageable);
}