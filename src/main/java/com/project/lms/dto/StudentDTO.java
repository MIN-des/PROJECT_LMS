package com.project.lms.dto;

import com.project.lms.constant.Dept;
import com.project.lms.constant.Gen;
import com.project.lms.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
  private String sId;
  private String sName;
  private String sPw;
  private String sTel;
  private String sAdd;
  private String sBirth;
  private String sEmail;
  private int grade;
  private Gen sGen;
  private Dept sDept;
  private Role role;
}

