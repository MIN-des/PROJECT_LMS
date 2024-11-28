package com.project.lms.dto;

import com.project.lms.entity.Dept;
import com.project.lms.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorDTO {

    private Long proNo;
    private Member proId;
    private String proRrn;
    private String proName;
    private String proEmail;
    private String proPhone;
    private String position;
    private Dept dept;
    private LocalDateTime yearEmp;
}
