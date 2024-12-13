package com.project.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TuitionInvoiceUploadDTO {
	private Long tId;
	private String sId; // 학번
	private String sName; // 학생 이름
	private String fileName; // 업로드된 파일명
	private String filePath; // 파일 경로
	private LocalDateTime uploadDate; // 업로드 날짜
}
