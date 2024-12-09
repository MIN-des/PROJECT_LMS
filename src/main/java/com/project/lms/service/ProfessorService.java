package com.project.lms.service;

import com.project.lms.dto.ProfessorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface ProfessorService extends UserDetailsService {

    ProfessorDTO createProfessor(ProfessorDTO professorDTO);

    // 관리자, 교수 계정이 교수 상세정보 확인하는 메소드
    Optional<ProfessorDTO> getProfessorById(String pId);

    ProfessorDTO updateProfessor(String pId, ProfessorDTO professorDTO);

    void deleteProfessor(String pId);

    Page<ProfessorDTO> searchProfessorsById(String pId, Pageable pageable);

    Page<ProfessorDTO> searchProfessorsByName(String pName, Pageable pageable);

    Page<ProfessorDTO> searchProfessorsByDept(String pDept, Pageable pageable);

    Page<ProfessorDTO> getAllProfessors(Pageable pageable);

    // 교수 계정이 내 정보 업데이트 하는 메소드
//    ProfessorUpdateDTO updateMyProfessor(String pID, ProfessorUpdateDTO updateDTO);
}
