package com.project.lms.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TuitionInvoiceUpload {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tId;

	@ManyToOne
	@JoinColumn(name = "sId", nullable = false)
	private Student student; // 학번으로 학생 연결

	private String fileName; // 업로드된 파일명
	private String filePath; // 서버에 저장된 파일 경로
	private LocalDateTime uploadDate; // 업로드 날짜

}
