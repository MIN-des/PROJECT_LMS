package com.project.lms.entity;

import com.project.lms.constant.Dept;
import com.project.lms.constant.Gen;
import com.project.lms.constant.Role;
import com.project.lms.dto.ProfessorDTO;
import com.project.lms.dto.ProfessorUpdateDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
public class Professor extends BaseEntity {

    @Id
    private String pId;
    private String pName;
    private String pPw;
    private String pTel; // 전화번호
    private String pAdd; // 주소
    private String pBirth; // 생년월일
    private String pEmail;
    private String year; // 입사년도

    @Enumerated(EnumType.STRING)
    private Gen pGen; // 성별

    @Enumerated(EnumType.STRING)
    private Dept pDept;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_PROFESSOR;

    // 업데이트 메소드(교수)
    public void updateMyProfessor(ProfessorUpdateDTO updateDTO) {
        this.pPw = updateDTO.getNewPw(); // 서비스에서 암호화 해야됨
        this.pTel = updateDTO.getNewTel();
        this.pAdd = updateDTO.getNewAdd();
    }

    // 업데이트 메소드(관리자)
/*    public void updateProfessor(ProfessorDTO professorDTO) {
        this.pId = professorDTO.getPId();
        this.pName = professorDTO.getPName();
        this.pPw = professorDTO.getPPw(); // 암호화 해야되나?
        this.pTel = professorDTO.getPTel();
        this.pAdd = professorDTO.getPAdd();
        this.pBirth = professorDTO.getPBirth();
        this.pEmail = professorDTO.getPEmail();
        this.year = professorDTO.getYear();
        this.pGen = professorDTO.getPGen();
        this.pDept = professorDTO.getPDept();
        this.role = professorDTO.getRole();*/
    }
