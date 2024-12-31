package com.project.lms.service;

import com.project.lms.dto.StudentDTO;
import com.project.lms.dto.TuitionInvoiceUploadDTO;
import com.project.lms.entity.TuitionInvoiceUpload;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TuitionInvoiceUploadService {

  TuitionInvoiceUpload getInvoiceById(Long tId);

  void uploadInvoice(String sId, MultipartFile file) throws Exception; // 관리자: 파일 업로드

  void deleteInvoice(Long tId) throws Exception; // 관리자: 파일 삭제

  List<TuitionInvoiceUploadDTO> getInvoicesByStudentId(String sId); // 학생: 파일 조회

  byte[] downloadInvoice(Long tId) throws Exception; // 학생: 파일 다운로드

  StudentDTO getStudentById(String id); // 학생 정보 가져오기

  boolean isInvoiceOwnedByStudent(Long tId, String sId);

}
