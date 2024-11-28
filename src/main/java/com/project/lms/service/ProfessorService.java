package com.project.lms.service;

import com.project.lms.dto.ProfessorDTO;
import org.springframework.http.ResponseEntity;

public interface ProfessorService {

    public ResponseEntity<ProfessorDTO> findByProId();
}
