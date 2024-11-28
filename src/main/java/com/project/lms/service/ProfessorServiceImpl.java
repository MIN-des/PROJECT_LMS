package com.project.lms.service;

import com.project.lms.dto.ProfessorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProfessorServiceImpl implements ProfessorService {
    @Override
    public ResponseEntity<ProfessorDTO> findByProId() {
        return null;
    }
}
