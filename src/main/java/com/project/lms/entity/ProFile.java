package com.project.lms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "proFile")
@Getter
@Setter
@ToString
public class ProFile {

	@Id
	@Column(nullable = false)
	private String uuid; // 파일 아이디

	@Column(nullable = false)
	private String fileName; // 파일명

	@Column(nullable = false)
	private String filePath; // 파일 경로

	private String fileType; // 파일 유형

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course; // 강좌 아이디 fk

}