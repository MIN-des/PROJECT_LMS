package com.project.lms.repository;

import com.project.lms.entity.Course;
import com.project.lms.entity.Order;
import com.project.lms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

  // 특정 강의 ID로 신청 내역 조회
  List<Order> findByCourse_cId(Long cId);

  List<Order> findByStudent_sId(String sId);

  boolean existsByStudentAndCourse(Student student, Course course);

}