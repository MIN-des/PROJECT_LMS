package com.project.lms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "takes")
@Getter
@Setter
@ToString
public class Takes {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long tno;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stu_id", nullable = false)
	private Student student; // 학생 아이디 fk

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cour_id", nullable = false)
	private Course course; // 강좌 아이디 fk

	private String score; // 성적 (A0 / A- / A+)

}