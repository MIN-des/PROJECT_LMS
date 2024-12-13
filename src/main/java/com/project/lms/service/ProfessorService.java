package com.project.lms.service;

import com.project.lms.dto.ProfessorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProfessorService {
    ProfessorDTO createProfessor(ProfessorDTO professorDTO);

    // 관리자, 교수 계정이 교수 상세정보 확인하는 메소드
    Optional<ProfessorDTO> getProfessorById(String pId);


}
