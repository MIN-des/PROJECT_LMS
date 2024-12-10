package com.project.lms.dto;

import com.project.lms.constant.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDTO {
	private String aId;
	private String aPw;
	private String aName;
	private Role role = Role.ROLE_ADMIN;
}
