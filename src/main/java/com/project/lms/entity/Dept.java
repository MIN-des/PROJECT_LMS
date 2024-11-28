package com.project.lms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "dept")
@Getter
@Setter
@ToString
public class Dept {

	@Id
	private String deptId; // 학과 아이디

	@Column(nullable = false)
	private String deptName; // 학과 이름

	@Column(nullable = false)
	private String office; // 학과 사무실

}