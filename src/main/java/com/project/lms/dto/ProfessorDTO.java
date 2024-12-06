package com.project.lms.dto;

import com.project.lms.constant.Dept;
import com.project.lms.constant.Gen;
import com.project.lms.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessorDTO {
	private String pId;
	private String pName;
	private String pPw;
	private String pTel;
	private String pAdd;
	private String pBirth;

	@Column(unique = true)
	private String pEmail;

	private String year;
	private Gen pGen;
	private Dept pDept;
	private Role role = Role.PROFESSOR;
}
