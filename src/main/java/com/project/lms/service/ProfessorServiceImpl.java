package com.project.lms.service;

import com.project.lms.constant.Dept;
import com.project.lms.dto.ProfessorDTO;
import com.project.lms.dto.ProfessorUpdateDTO;
import com.project.lms.entity.Professor;
import com.project.lms.repository.ProfessorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
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

        if (professorRepository.existsBypEmail(professorDTO.getPEmail())) {
            throw new IllegalArgumentException("이미 중복된 이메일이 존재합니다.");
        }

        Professor professor = modelMapper.map(professorDTO, Professor.class);
        return modelMapper.map(professorRepository.save(professor), ProfessorDTO.class);
    }

    // 관리자, 교수가 교수 세부정보 조회
    @Override
    public Optional<ProfessorDTO> getProfessorById(String pId) {
        return professorRepository.findById(pId)
                .map(professor -> modelMapper.map(professor, ProfessorDTO.class));
    }

    // 관리자가 교수 정보 업데이트
    @Override
    public ProfessorDTO updateProfessor(String pId, ProfessorDTO professorDTO) {
        Professor professor = professorRepository.findById(pId)
                .orElseThrow(() -> new EntityNotFoundException("Professor with id " + pId + " not found"));

        // 교직원 번호 중복 확인
        if (!professor.getPId().equals(professorDTO.getPId()) && professorRepository.existsById(professorDTO.getPId())) {
            throw new IllegalArgumentException("이미 중복된 교직원 번호가 존재합니다: " + professorDTO.getPId());
        }

        // 이메일 중복 확인
        if (!professor.getPEmail().equals(professorDTO.getPEmail()) && professorRepository.existsBypEmail(professorDTO.getPEmail())) {
            throw new IllegalArgumentException("이미 중복된 이메일이 존재합니다: " + professorDTO.getPEmail());
        }

        modelMapper.map(professorDTO, professor);
        return modelMapper.map(professorRepository.save(professor), ProfessorDTO.class);
    }

    @Override
    public void deleteProfessor(String pId) {
        professorRepository.deleteById(pId);
    }

    // 교수 계정이 내 정보 업데이트 하는 메소드
    @Override
    public String updateMyProfessor(ProfessorUpdateDTO updateDTO) throws Exception {
        Professor professor = professorRepository.findById(updateDTO.getPId()).orElseThrow(EntityNotFoundException::new);

        // 새 비밀번호가 제공된 경우에만 암호화 처리
        if (updateDTO.getNewPw() != null && !updateDTO.getNewPw().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(updateDTO.getNewPw());
            updateDTO.setNewPw(encodedPassword);
        }

        professor.updateMyProfessor(updateDTO);

        return professor.getPId();
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
            // pDept 를 Enum 타입으로 변환
            Dept dept = Dept.valueOf(pDept.toUpperCase()); // 유효하지 않은 값이면 예외 발생
            return professorRepository.findBypDept(dept, pageable) // 변환된 Dept 타입 사용
                    .map(professor -> modelMapper.map(professor, ProfessorDTO.class));
        } catch (IllegalArgumentException e) {
            // 변환 실패 시 예외 처리
            throw new IllegalArgumentException("유효하지 않은 학부 값입니다.");
        }
    }

    @Override
    public Page<ProfessorDTO> getAllProfessors(Pageable pageable) {
        return professorRepository.findAll(pageable)
                .map(professor -> modelMapper.map(professor, ProfessorDTO.class));
    }
}