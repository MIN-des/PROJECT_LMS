package com.project.lms.service;

import com.project.lms.dto.ProfessorDTO;
import com.project.lms.dto.ProfessorUpdateDTO;
import com.project.lms.entity.Professor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.Valid;

public interface ProfessorService {

    // 관리자, 교수가 정보 조회하는 메소드
    public ProfessorDTO getProfessor(String pId);

    // 관리자가 교수 정보 업데이트 하는 메소드(비밀번호 암호화 하지 않음?)
    public String updateProfessor(ProfessorDTO professorDTO) throws Exception;

    // 교수 계정이 내 정보 업데이트 하는 메소드
    public String updateMyProfessor(ProfessorUpdateDTO updateDTO) throws Exception;

    // 관리자가 교수 계정 삭제하는 메소드는 Repository를 통해 실행
    // public void deleteProfessor(ProfessorDTO professorDTO);

    public Professor saveProfessor(Professor professor);
}
