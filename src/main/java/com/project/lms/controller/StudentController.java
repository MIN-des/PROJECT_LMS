package com.project.lms.controller;

import com.project.lms.dto.StudentDTO;
import com.project.lms.dto.TuitionInvoiceUploadDTO;
import com.project.lms.entity.Student;
import com.project.lms.entity.TuitionInvoiceUpload;
import com.project.lms.repository.StudentRepository;
import com.project.lms.service.EnrollService;
import com.project.lms.service.StudentService;
import com.project.lms.service.TuitionInvoiceUploadService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

  private static final Logger log = LoggerFactory.getLogger(StudentController.class);
  private final StudentService studentService;
  private final TuitionInvoiceUploadService tuitionInvoiceUploadService;
  private final StudentRepository studentRepository;
  private final PasswordEncoder passwordEncoder;

  @GetMapping("/info")
  public String getStudentInfo(Model model, Principal principal) {
    String sId = principal.getName(); // Principal에서 sId 가져오기
    System.out.println("Logged-in student ID: " + sId);

    Student student = studentRepository.findById(sId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found for ID: " + sId));

    model.addAttribute("student", student);

    return "student/info";
  }

  // 교수 자신의 정보 업데이트 (주소, 전화번호, 비밀번호)
  @PostMapping("/info")
  public String updateStudentInfo(
          @Valid @ModelAttribute("student") Student updatedStudent,
          BindingResult bindingResult,
          @RequestParam(required = false) String newPw,
          @RequestParam(required = false) String newSTel,
          @RequestParam(required = false) String newSAdd,
          Principal principal,
          RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      return "student/info"; // 에러 발생 시 원래 페이지로
    }

    String sId = principal.getName();
    Student student = studentRepository.findById(sId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found for ID: " + sId));

    // 비밀번호 업데이트
    if (newPw != null && !newPw.trim().isEmpty()) {
      student.setSPw(passwordEncoder.encode(newPw));
    }

    // 전화번호 업데이트
    if (newSTel != null && !newSTel.trim().isEmpty()) {
      student.setSTel(newSTel);
    }

    // 주소 업데이트
    if (newSAdd != null && !newSAdd.trim().isEmpty()) {
      student.setSAdd(newSAdd);
    }

    // 비밀번호가 업데이트되었을 경우 암호화 처리
    if (updatedStudent.getSPw() != null && !updatedStudent.getSPw().isEmpty()) {
      student.setSPw(passwordEncoder.encode(updatedStudent.getSPw()));
    }

    studentRepository.save(student);

    redirectAttributes.addFlashAttribute("successMessage", "정보가 수정되었습니다.");

    return "redirect:/student/info"; // 리다이렉트
  }

  @GetMapping("/modify/{sId}")
  public String updateInfoForm(@PathVariable String sId, Model model) {
    Student student = studentService.getStudentInfo(sId);
    model.addAttribute("student", student);
    return "student/modify";
  }

  @PostMapping("/modify/{sId}")
  public String updateInfo(@PathVariable String sId, @Valid @ModelAttribute StudentDTO studentDTO, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("student", studentDTO);
      return "student/modify"; // 유효성 검사 실패 시 폼으로 돌아가기
    }
    try {
      studentService.updateInfo(sId, studentDTO);
    } catch (IllegalArgumentException e) {
      model.addAttribute("errorMessage", e.getMessage());
      model.addAttribute("student", studentDTO);
      return "student/modify"; // 오류발생시 수정 페이지로 돌아가지
    }
    Student updatedInfo = studentService.updateInfo(sId, studentDTO);
    return "redirect:/student/info/";
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
  @GetMapping("/invoices/download/{tId}")
  public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long tId, Authentication authentication) throws Exception {

    if (authentication == null || authentication.getName() == null) {
      throw new IllegalStateException("사용자 인증 정보가 누락되었습니다.");
    }

    // 인증된 사용자 정보에서 학생 ID 가져오기
    String sId = authentication.getName();
    log.info("Authenticated student ID: {}", sId);

    if (!tuitionInvoiceUploadService.isInvoiceOwnedByStudent(tId, sId)) {
      log.warn("Unauthorized access attempt by student ID: {} for invoice ID: {}", sId, tId);
      throw new AccessDeniedException("접근 권한이 없습니다.");
    }

    // tId의 유효성 검증
    if (tId == null || tId <= 0) {
      throw new IllegalArgumentException("tId가 유효하지 않습니다.");
    }

    // 해당 학생이 tId를 소유하고 있는지 확인
    if (!tuitionInvoiceUploadService.isInvoiceOwnedByStudent(tId, sId)) {
      log.warn("Unauthorized access attempt by student ID: {} for invoice ID: {}", sId, tId);
      throw new AccessDeniedException("접근 권한이 없습니다.");
    }
    // 등록금 고지서 엔티티 가져오기
    TuitionInvoiceUpload invoice = tuitionInvoiceUploadService.getInvoiceById(tId);

    // 엔티티에서 파일 이름 가져오기
    String originalFileName = invoice.getFileName();

    // 파일 이름 UTF-8로 인코딩
    String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8.toString()).replace("+", "%20");

    byte[] fileData = tuitionInvoiceUploadService.downloadInvoice(tId);
    log.info("File downloaded successfully for invoice ID: {}", tId);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", encodedFileName);

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

  @GetMapping("/grades")
  public String getStudentGrades(Principal principal, Model model) {
    // 로그인한 학생 ID
    String sId = principal.getName();

    // 학생의 수강 내역과 성적 가져오기
    Map<String, Object> studentData = studentService.getOrdersWithScores(sId);

    // 모델에 데이터 추가
    model.addAttribute("student", studentData.get("student"));
    model.addAttribute("orders", studentData.get("orders"));

    return "student/grades"; // 성적 확인 화면으로 이동
  }
}
