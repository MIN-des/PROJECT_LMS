package com.project.lms.controller;

import com.project.lms.dto.StudentDTO;
import com.project.lms.entity.Student;
import com.project.lms.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

  private final StudentService studentService;

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
