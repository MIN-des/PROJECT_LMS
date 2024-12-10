package com.project.lms.controller;

import com.project.lms.dto.StudentDTO;
import com.project.lms.dto.TuitionInvoiceUploadDTO;
import com.project.lms.entity.Student;
import com.project.lms.service.StudentService;
import com.project.lms.service.TuitionInvoiceUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

  private final StudentService studentService;
  private final TuitionInvoiceUploadService tuitionInvoiceUploadService;

  @GetMapping("/info/{sId}")
  public String getStudentById(@PathVariable String sId, Model model) {
    // 서비스 호출하여 학생 정보 조회
    Student student = studentService.getStudentInfo(sId);
    model.addAttribute("student", student);
    return "student/info";
  }

  @GetMapping("/modify/{sId}")
  public String updateInfoForm(@PathVariable String sId,Model model) {
    Student student = studentService.getStudentInfo(sId);
    model.addAttribute("student", student);
    return "student/modify";
  }

  @PostMapping("/modify/{sId}")
  public String updateInfo(@PathVariable String sId, @Valid @ModelAttribute StudentDTO studentDTO, BindingResult bindingResult, Model model) {
    if(bindingResult.hasErrors()) {
      model.addAttribute("student", studentDTO);
      return "student/modify"; // 유효성 검사 실패 시 폼으로 돌아가기
    }
    Student updatedInfo = studentService.updateInfo(sId, studentDTO);
    return "redirect:/student/info/" + updatedInfo.getSId();
  }

  // 등록금 고지서 목록 조회
  @GetMapping("/invoices")
  public String getMyInvoices(Model model) {
    // 현재 로그인된 사용자 정보 가져오기
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String sId = authentication.getName(); // 로그인된 학생의 ID (학번)

    // 학번을 사용하여 고지서 목록 조회
    List<TuitionInvoiceUploadDTO> invoices = tuitionInvoiceUploadService.getInvoicesByStudentId(sId);
    model.addAttribute("invoices", invoices);

    return "student/invoices"; // 학생용 고지서 목록 뷰
  }


  // 등록금 고지서 다운로드
  @GetMapping("/invoices/{sId}/download/{tId}")
  public ResponseEntity<byte[]> downloadInvoice(@PathVariable String sId, @PathVariable Long tId) throws Exception {
    // sId와 tId의 유효성 검증
    if (sId == null || sId.isEmpty()) {
      throw new IllegalArgumentException("학생 ID가 유효하지 않습니다.");
    }
    if (tId == null || tId <= 0) {
      throw new IllegalArgumentException("tId가 유효하지 않습니다.");
    }

    byte[] fileData = tuitionInvoiceUploadService.downloadInvoice(tId);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "invoice_" + tId + ".pdf");

    return ResponseEntity.ok()
        .headers(headers)
        .body(fileData);
  }

  // 등록금 고지서 미리보기
  @GetMapping("/invoices/preview/{tId}")
  public ResponseEntity<byte[]> previewInvoice(@PathVariable Long tId) throws Exception {

    // 등록금 고지서 다운로드 서비스 호출
    byte[] fileData = tuitionInvoiceUploadService.downloadInvoice(tId);

    // HTTP 응답 설정
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF); // 브라우저가 PDF로 렌더링하도록 Content-Type 설정
    return ResponseEntity.ok()
        .headers(headers)
        .body(fileData);
  }

//  @GetMapping("/info")
//  public Student getMyInfo() {
//    // 현재 인증된 사용자의 ID 가져오기
//    String sId = getCurrentUserId();
//
//    // 학생 정보 조회
//    return studentService.getStudentInfo(sId);
//  }
//
//  private String getCurrentUserId() {
//    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//    if (principal instanceof UserDetails) {
//      return ((UserDetails) principal).getUsername(); // 인증된 사용자의 ID
//    } else {
//      throw new IllegalArgumentException("User not authenticated");
//    }
//  }

}
