package com.project.lms.repository;

import com.project.lms.dto.ProfessorDTO;
import com.project.lms.constant.Dept;
import com.project.lms.entity.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, String> {
    Professor findBypId(String pId);

    Page<Professor> findAll(Pageable pageable);

    Page<Professor> findBypIdContainingIgnoreCase(String pId, Pageable pageable);

    Page<Professor> findBypNameContainingIgnoreCase(String pName, Pageable pageable);

    Page<Professor> findBypDept(Dept pDept, Pageable pageable);

    boolean existsById(String pId);

    boolean existsBypEmail(String pEmail);
}
