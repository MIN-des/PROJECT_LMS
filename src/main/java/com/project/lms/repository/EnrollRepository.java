package com.project.lms.repository;

import com.project.lms.entity.Enroll;
import com.project.lms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollRepository extends JpaRepository<Enroll, Long> {
  Optional<Enroll> findByStudent(Student student);


}
