package com.project.lms.service;

import com.project.lms.dto.ProfessorDTO;
import com.project.lms.entity.Professor;
import com.project.lms.repository.ProfessorRepository;
import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public ProfessorDTO createProfessor(ProfessorDTO professorDTO) {
        if (professorRepository.existsById(professorDTO.getPId())) {
            throw new IllegalArgumentException("이미 중복된 교직원 번호가 존재합니다.");
        }

        // 비밀번호 암호화
        if (professorDTO.getPPw() != null && !professorDTO.getPPw().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(professorDTO.getPPw());
            professorDTO.setPPw(encodedPassword);
        }

        if (professorRepository.existsBypEmail(professorDTO.getPEmail())) {
            throw new IllegalArgumentException("이미 중복된 이메일이 존재합니다.");
        }

        Professor professor = modelMapper.map(professorDTO, Professor.class);
        return modelMapper.map(professorRepository.save(professor), ProfessorDTO.class);
    }
}