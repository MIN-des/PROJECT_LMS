package com.project.lms.service;

import com.project.lms.dto.ProfessorDTO;
import com.project.lms.dto.ProfessorUpdateDTO;
import com.project.lms.entity.Professor;
import com.project.lms.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

// ProfessorUpdateDTO에서 작성한 어노테이션을 적용시키기 위해서 updateProfessor()에 @Valid 작성
// 하지만 유효성 검사가 서비스 계층에서 활성화되지 못하는 문제가 발생함
// 해결 방안으로는 컨트롤러 레이어에서 예외 처리를 하거나
// 서비스 계층에 @Validated 어노테이션을 추가해야 함
// @Validated
@Service
@Transactional
@RequiredArgsConstructor
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;

    // 관리자가 교수 정보 조회하는 메소드
    @Override
    public ProfessorDTO getProfessor(String pId) {
        Professor professor = professorRepository.findById(pId).orElseThrow(EntityNotFoundException::new);
        ProfessorDTO professorDTO = ProfessorDTO.of(professor);

        return professorDTO;
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

    // 관리자가 교수 정보 업데이트 하는 메소드(비밀번호 암호화 하지 않음?)
    @Override
    public String updateProfessor(ProfessorDTO professorDTO) throws Exception {
        Professor professor = professorRepository.findById(professorDTO.getPId()).orElseThrow(EntityNotFoundException::new);
        professor.updateProfessor(professorDTO);

        return professor.getPId();
    }

    /*@Override
    public void deleteProfessor(ProfessorDTO professorDTO) {
        Professor professor = professorRepository.findById(professorDTO.getPId()).orElseThrow(EntityNotFoundException::new);
        professorRepository.delete(professor);
    }*/

    // 관리자 교수 계정 생성하는 메소드
    @Override
    public Professor saveProfessor(Professor professor) {
        // 중복 검사 메소드 호출 후 중복일 경우, 에러 메시지 출력
        validateDuplicateProfessor(professor);

        // 중복 아닐 경우, 생성한 계정 저장
        return professorRepository.save(professor);
    }

    // 관리자가 계정 생성 시, 아이디 중복 검사하는 메소드
    private void validateDuplicateProfessor(Professor professor) {
        Professor findByProfessor = professorRepository.findBypId(professor.getPId());

        if (findByProfessor != null) {
            throw new IllegalStateException("이미 등록된 계정입니다.");
        }
    }
}
