package com.project.lms.controller;

import com.project.lms.dto.StudentDTO;
import com.project.lms.entity.Professor;
import com.project.lms.entity.Student;
import com.project.lms.repository.StudentRepository;
import com.project.lms.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

  private final StudentService studentService;
  private final StudentRepository studentRepository;
  private final PasswordEncoder passwordEncoder;

  @GetMapping("/info")
  public String getStudentInfo(Model model, Principal principal) {
    String sId = principal.getName(); // Principal에서 pId 가져오기
    System.out.println("Logged-in professor ID: " + sId);

    Student student = studentRepository.findById(sId)
            .orElseThrow(() -> new IllegalArgumentException("Professor not found for ID: " + sId));

    model.addAttribute("student", student);

    return "student/info";
  }

  // 교수 자신의 정보 업데이트 (주소, 전화번호, 비밀번호)
  @PostMapping("/info")
  public String updateStudentInfo(@Valid @ModelAttribute("student") Student updatedStudent,
                                    BindingResult bindingResult,
                                    Principal principal, RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      return "student/info"; // 에러 발생 시 원래 페이지로
    }

    String sId = principal.getName(); // 로그인된 교수의 ID 가져오기
    Student student = studentRepository.findById(sId)
            .orElseThrow(() -> new IllegalArgumentException("Professor not found for ID: " + sId));

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
    try{
      studentService.updateInfo(sId, studentDTO);
    } catch (IllegalArgumentException e) {
      model.addAttribute("errorMessage", e.getMessage());
      model.addAttribute("student", studentDTO);
      return "student/modify"; // 오류발생시 수정 페이지로 돌아가지
    }
    Student updatedInfo = studentService.updateInfo(sId, studentDTO);
    return "redirect:/student/info/";
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
