package com.project.lms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "allBoard")
@Getter
@Setter
@ToString
//@EntityListeners(AuditingEntityListener.class)
public class AllBoard extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long allBno; // 전체 게시판 번호(정렬)

	@Column(nullable = false)
	private String allTitle; // 제목

	@Column(nullable = false,columnDefinition = "CLOB")
	private String allContent; // 내용

	@Column(nullable = false)
	private String allWriter; // 작성자

	@Column(nullable = false, columnDefinition = "INT DEFAULT 0")
	private int allViews; // 조회수

//	@OneToMany(mappedBy = "allBoard", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<AllBoardFile> fileList = new ArrayList<>();// 업로드 파일 리스트

}
