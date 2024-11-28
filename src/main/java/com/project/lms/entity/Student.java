package com.project.lms.entity;

import com.project.lms.constant.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "student")
@Getter
@Setter
@ToString
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Member stuId; // 학생 아이디

	@Column(unique = true, nullable = false)
	private String stuRrn; // 학생 주민등록번호

	@Column(nullable = false)
	private String stuName; // 학생 이름

	@Column(unique = true, nullable = false)
	private String stuEmail; // 학생 이메일

	@Column(nullable = false)
	private String stuPhone; // 전화번호, 010-0000-0000(- 포함 13자)

	@Column(nullable = false)
	private int grade; // 학년

	private String address; // 주소

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_id", nullable = false)
	private Dept dept; // 학과번호 fk

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status; // 재학 상태 (재학, 복학, 휴학)

}
