package com.project.lms.dto;

import com.project.lms.constant.Dept;
import com.project.lms.constant.Gen;
import com.project.lms.constant.Role;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {
  private String sId;
  private String sName;
  private String sPw;
  private String sTel;
  private String sAdd;
  private String sBirth;

  @Column(unique = true)
  private String sEmail;

  private int grade;
  private Gen sGen;

  @Enumerated(EnumType.STRING)
  private Dept sDept;

  private Role role = Role.ROLE_STUDENT;
}
