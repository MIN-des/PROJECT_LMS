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
    private Role role;

    // 업데이트 메소드(교수)
    public void updateMyProfessor(ProfessorUpdateDTO updateDTO) {
        this.pPw = updateDTO.getNewPw(); // 서비스에서 암호화 해야됨
        this.pTel = updateDTO.getNewTel();
        this.pAdd = updateDTO.getNewAdd();
    }

    // 업데이트 메소드(관리자)
    public void updateProfessor(ProfessorDTO professorDTO) {
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
        this.role = professorDTO.getRole();
    }

    // 관리자가 생성하는 경우
    // Entity가 많을 경우 ModelMapper를 사용하게 되면 modelMapper가 잘못 매칭할 수 있고,
    // 명시적이고 직관적이게 표현하기 위해 Entity에서 구현함
    public static Professor createProfessor(
            ProfessorDTO professorDTO, PasswordEncoder passwordEncoder) {

        Professor professor = new Professor();
        String password = passwordEncoder.encode(professorDTO.getPPw());

        professor.setPId(professorDTO.getPId());
        professor.setPName(professorDTO.getPName());
        professor.setPPw(password);
        professor.setPTel(professorDTO.getPTel());
        professor.setPAdd(professorDTO.getPAdd());
        professor.setPBirth(professorDTO.getPBirth());
        professor.setPEmail(professorDTO.getPEmail());
        professor.setYear(professorDTO.getYear());
        professor.setPGen(professorDTO.getPGen());
        professor.setPDept(professorDTO.getPDept());
        professor.setRole(professorDTO.getRole());

        return professor;
    }
}