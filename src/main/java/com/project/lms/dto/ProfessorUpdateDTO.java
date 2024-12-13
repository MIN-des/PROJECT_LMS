package com.project.lms.dto;

import com.project.lms.entity.Professor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

//// 사용하지 않는 파일

@Getter
@Setter
public class ProfessorUpdateDTO {

    private String pId;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 최소 8자리 이상, 16자리 이하이어야 합니다.")
    private String newPw;

    @NotBlank(message = "전화번호는 필수 항목입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호는 '-'를 포함하여 입력해야 합니다. 예: 010-1234-5678")
    private String newTel;

    private String newAdd;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ProfessorUpdateDTO of(Professor professor) {
        return modelMapper.map(professor, ProfessorUpdateDTO.class);
    }
}

