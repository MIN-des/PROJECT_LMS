package com.project.lms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "course")
@Getter
@Setter
@ToString
public class Course {

	@Id
	@Column(nullable = false)
	private String courId; // 강좌 아이디

	@Column(nullable = false)
	private String courTitle; // 강좌명

	private String credit; // 학점(A-, A+, A0)

	private String year; // 연도

	private int semester; // 학기

	private String division; // 분반

	private String classroom; // 강의실

	private int enroll; // 수강인원

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pro_id", nullable = false)
	private Professor professor;

}
