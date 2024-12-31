package com.project.lms.repository;

import com.project.lms.constant.Dept;
import com.project.lms.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
  Page<Student> findAll(Pageable pageable);

  Page<Student> findBysIdContainingIgnoreCase(String sId, Pageable pageable);

  Page<Student> findBysNameContainingIgnoreCase(String sName, Pageable pageable);

  Page<Student> findBysDept(Dept sDept, Pageable pageable);

  boolean existsById(String sId);

  boolean existsBysEmail(String sEmail);

  boolean existsBysTel(String sTel);

}
