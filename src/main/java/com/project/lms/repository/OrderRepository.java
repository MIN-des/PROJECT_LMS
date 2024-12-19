package com.project.lms.repository;

import com.project.lms.entity.Course;
import com.project.lms.entity.Order;
import com.project.lms.entity.Professor;
import com.project.lms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 학생이 수강 신청한 교수 목록
    @Query("SELECT DISTINCT o.course.professor FROM Order o WHERE o.student.sId = :sId")
    List<Professor> findProfessorsByStudent_sId(@Param("sId") String sId);

    // 교수가 담당하는 강의를 수강 신청한 학생 목록
    @Query("SELECT DISTINCT o.student FROM Order o WHERE o.course.professor.pId = :pId")
    List<Student> findStudentsByProfessor_pId(@Param("pId") String pId);

    // 특정 강의 ID로 신청 내역 조회
    List<Order> findByCourse_cId(Long cId);

    List<Order> findByStudent_sId(String sId);

    boolean existsByStudentAndCourse(Student student, Course course);
}