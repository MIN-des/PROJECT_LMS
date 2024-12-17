package com.project.lms.repository;

import com.project.lms.entity.Enroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollRepository extends JpaRepository<Enroll, Long> {
//  @Query("SELECT e FROM enroll e WHERE e.student.sId = :studentId")
  Optional<Enroll> findByStudent_sId(/*@Param("studentId")*/ String studentId);
}
