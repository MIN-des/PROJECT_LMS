package com.project.lms.repository;

import com.project.lms.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// CRUD를 어떻게 처리할지 정의하는 계층
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    List<Professor> findByProId(String proId);
}
