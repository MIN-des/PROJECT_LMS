package com.project.lms.repository;

import com.project.lms.constant.RestStatus;
import com.project.lms.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

  @Query("SELECT c.createdBy FROM Course c WHERE c.cId = :cId")
  Optional<String> findCreatedBy(@Param("cId") Long cId);

  // 모든 강의 목록 조회
  Page<Course> findAll(Pageable pageable);

  // 강의 ID로 검색하는 경우, 한 글자만 포함되어도 페이징 처리되어 조회
  Page<Course> findBycId(Long cId, Pageable pageable);

  // 강의명으로 검색하는 경우, 한 글자만 포함되어도 페이징 처리되어 조회
  Page<Course> findBycNameContainingIgnoreCase(String cName, Pageable pageable);

  Page<Course> findByCreatedByContainingIgnoreCase(String createdBy, Pageable pageable);

  // 잔여상태로 검색하는 경우
  Page<Course> findByStatus(RestStatus status, Pageable pageable);

  boolean existsBycId(Long cId);

  boolean existsBycName(String cName);
}