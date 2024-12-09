package com.project.lms.service;

import com.project.lms.constant.Dept;
import com.project.lms.dto.ProfessorDTO;
import com.project.lms.entity.Professor;
import com.project.lms.repository.ProfessorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

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

    // 나머지 메서드는 기존 코드 유지
    @Override
    public Optional<ProfessorDTO> getProfessorById(String pId) {
        return professorRepository.findById(pId)
                .map(professor -> modelMapper.map(professor, ProfessorDTO.class));
    }

    @Override
    public ProfessorDTO updateProfessor(String pId, ProfessorDTO professorDTO) {
        Professor professor = professorRepository.findById(pId)
                .orElseThrow(() -> new EntityNotFoundException("Professor with id " + pId + " not found"));

        if (!professor.getPId().equals(professorDTO.getPId()) && professorRepository.existsById(professorDTO.getPId())) {
            throw new IllegalArgumentException("이미 중복된 교직원 번호가 존재합니다: " + professorDTO.getPId());
        }

        if (!professor.getPEmail().equals(professorDTO.getPEmail()) && professorRepository.existsBypEmail(professorDTO.getPEmail())) {
            throw new IllegalArgumentException("이미 중복된 이메일이 존재합니다: " + professorDTO.getPEmail());
        }

        // 비밀번호 암호화
        if (professorDTO.getPPw() != null && !professorDTO.getPPw().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(professorDTO.getPPw());
            professorDTO.setPPw(encodedPassword);
        }

        modelMapper.map(professorDTO, professor);
        return modelMapper.map(professorRepository.save(professor), ProfessorDTO.class);
    }

    @Override
    public void deleteProfessor(String pId) {
        professorRepository.deleteById(pId);
    }

    @Override
    public Page<ProfessorDTO> searchProfessorsById(String pId, Pageable pageable) {
        return professorRepository.findBypIdContainingIgnoreCase(pId, pageable)
                .map(professor -> modelMapper.map(professor, ProfessorDTO.class));
    }

    @Override
    public Page<ProfessorDTO> searchProfessorsByName(String pName, Pageable pageable) {
        return professorRepository.findBypNameContainingIgnoreCase(pName, pageable)
                .map(professor -> modelMapper.map(professor, ProfessorDTO.class));
    }

    @Override
    public Page<ProfessorDTO> searchProfessorsByDept(String pDept, Pageable pageable) {
        try {
            Dept dept = Dept.valueOf(pDept.toUpperCase());
            return professorRepository.findBypDept(dept, pageable)
                    .map(professor -> modelMapper.map(professor, ProfessorDTO.class));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 학부 값입니다.");
        }
    }

    @Override
    public Page<ProfessorDTO> getAllProfessors(Pageable pageable) {
        return professorRepository.findAll(pageable)
                .map(professor -> modelMapper.map(professor, ProfessorDTO.class));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Professor professor = professorRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Professor not found"));

        return User.builder()
                .username(professor.getPId())
                .password(professor.getPPw())
                .roles("PROFESSOR")
                .build();
    }
}