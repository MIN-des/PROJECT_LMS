package com.project.lms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "professor")
@Getter
@Setter
@ToString
public class Professor {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long proNo;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Member proId; // 교수 아이디

	@Column(unique = true, nullable = false)
	private String proRrn; // 주민등록번호

	@Column(nullable = false)
	private String proName; // 교수 이름

	@Column(unique = true, nullable = false)
	private String proEmail; // 교수 이메일

	@Column(nullable = false)
	private String proPhone; // 전화번호, 010-0000-0000(- 포함 13자)

	@Column(nullable = false)
	private String position; // 직위(ex.교수, 부교수, 조교수)

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_id", nullable = false)
	private Dept dept; // 학과 아이디 fk

	@CreatedDate
	private LocalDateTime yearEmp; // 임용년도

}