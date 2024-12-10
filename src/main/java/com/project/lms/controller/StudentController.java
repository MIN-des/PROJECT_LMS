package com.project.lms.controller;

import com.project.lms.dto.StudentDTO;
import com.project.lms.dto.TuitionInvoiceUploadDTO;
import com.project.lms.entity.Student;
import com.project.lms.repository.StudentRepository;
import com.project.lms.service.StudentService;
import com.project.lms.service.TuitionInvoiceUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final TuitionInvoiceUploadService tuitionInvoiceUploadService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/info")
    public String getProfessorInfo(Model model, Principal principal) {
        String sId = principal.getName(); // Principal에서 pId 가져오기
        System.out.println("Logged-in student ID: " + sId);

        Student student = studentRepository.findById(sId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found for ID: " + sId));

        model.addAttribute("student", student);

        return "student/info";
    }

    // 학생 자신의 정보 업데이트 (주소, 전화번호, 비밀번호)
    @PostMapping("/info")
    public String updateStudentInfo(@Valid @ModelAttribute("student") Student updatedStudent,
                                    BindingResult bindingResult,
                                    Principal principal, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "student/info"; // 에러 발생 시 원래 페이지로
        }

        String sId = principal.getName(); // 로그인된 교수의 ID 가져오기
        Student student = studentRepository.findById(sId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found for ID: " + sId));

        // 비밀번호가 업데이트되었을 경우 암호화 처리
        if (updatedStudent.getSPw() != null && !updatedStudent.getSPw().isEmpty()) {
            student.setSPw(passwordEncoder.encode(updatedStudent.getSPw()));
        }

        // 업데이트 가능한 필드 업데이트
        student.setSTel(updatedStudent.getSTel());
        student.setSAdd(updatedStudent.getSAdd());

        studentRepository.save(student);

        redirectAttributes.addFlashAttribute("successMessage", "정보가 수정되었습니다.");

        return "redirect:/student/info"; // 리다이렉트
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
