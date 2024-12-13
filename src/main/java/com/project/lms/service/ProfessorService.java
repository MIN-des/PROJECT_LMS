package com.project.lms.service;

import com.project.lms.dto.ProfessorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProfessorService {
    ProfessorDTO createProfessor(ProfessorDTO professorDTO);
}
