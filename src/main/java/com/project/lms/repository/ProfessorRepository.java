package com.project.lms.repository;

import com.project.lms.dto.ProfessorDTO;
import com.project.lms.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, String> {

    Professor findBypId(String pId);
}