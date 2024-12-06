package com.project.lms.dto;

import com.project.lms.constant.Dept;
import com.project.lms.constant.Gen;
import com.project.lms.constant.Role;
import com.project.lms.entity.Professor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ProfessorDTO {

    // professor only read
    // professor only (pw, tel, add) update
    private String pId;

    @NotBlank(message = "이름은 필수 항목입니다.")
    private String pName;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String pPw;

    private String pTel; // 전화번호
    private String pAdd; // 주소

    @NotBlank(message = "생년월일은 필수 항목입니다.")
    private String pBirth; // 생년월일

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "이메일 형식으로 입력해야 합니다. 예: professor@edu.com")
    private String pEmail;

    @NotBlank(message = "입사년도는 필수 항목입니다.")
    private String year; // 입사년도

    @NotBlank(message = "성별은 필수 항목입니다.")
    private Gen pGen; // 성별

    private Dept pDept;

    @NotBlank(message = "계정 권한은 필수 항목입니다.")
    private Role role;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ProfessorDTO of(Professor professor) {
        return modelMapper.map(professor, ProfessorDTO.class);
    }
}
