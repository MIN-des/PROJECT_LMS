package com.project.lms.repository;

import com.project.lms.entity.TuitionInvoiceUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TuitionInvoiceUploadRepository extends JpaRepository<TuitionInvoiceUpload, Long> {
	List<TuitionInvoiceUpload> findByStudent_sId(String sId); // 학번으로 등록금 고지서 검색
}
