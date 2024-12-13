package com.project.lms.repository;

import com.project.lms.entity.Course;
import com.project.lms.entity.Order;
import com.project.lms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStudent_sId(String sId);

    boolean existsByStudentAndCourse(Student student, Course course);
}
