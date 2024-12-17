package com.project.lms.controller;

import com.project.lms.dto.CourseDTO;
import com.project.lms.entity.Course;
import com.project.lms.entity.Professor;
import com.project.lms.repository.ProfessorRepository;
import com.project.lms.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/professor")
@RequiredArgsConstructor
public class ProfessorController {

  private final ProfessorRepository professorRepository;
  private final CourseService courseService;
  private final PasswordEncoder passwordEncoder;

  // 로그인 후 교수 자신의 정보 조회
  @GetMapping("/info")
  public String getProfessorInfo(Model model, Principal principal) {
    String pId = principal.getName(); // Principal에서 pId 가져오기
    System.out.println("Logged-in professor ID: " + pId);

    Professor professor = professorRepository.findById(pId)
            .orElseThrow(() -> new IllegalArgumentException("Professor not found for ID: " + pId));

    model.addAttribute("professor", professor);

    return "professor/info";
  }

  // 교수 내정보 수정 페이지 이동
  @GetMapping("/modify")
  public String modifyProfessorInfo(Model model, Principal principal) {
    String pId = principal.getName(); // 로그인된 교수의 ID 가져오기
    Professor professor = professorRepository.findById(pId)
            .orElseThrow(() -> new IllegalArgumentException("Professor not found for ID: " + pId));

    model.addAttribute("professor", professor); // 교수 정보 전달
    return "professor/modify"; // modify.html 렌더링
  }

  // 교수 내정보 수정 처리
  @PostMapping("/modify/{pId}")
  public String updateProfessorInfo(
          @Valid @ModelAttribute("professor") Professor updatedProfessor,
          BindingResult bindingResult,
          @RequestParam(required = false) String newPw, // 새 비밀번호
          @RequestParam(required = false) String newPTel, // 새 전화번호
          @RequestParam(required = false) String newPAdd, // 새 주소
          Principal principal,
          RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      return "professor/modify"; // 에러 발생 시 다시 수정 페이지로 이동
    }

    String pId = principal.getName(); // 로그인된 교수 ID
    Professor professor = professorRepository.findById(pId)
            .orElseThrow(() -> new IllegalArgumentException("Professor not found for ID: " + pId));

    // 비밀번호 업데이트
    if (newPw != null && !newPw.trim().isEmpty()) {
      professor.setPPw(passwordEncoder.encode(newPw));
    }

    // 전화번호 업데이트
    if (newPTel != null && !newPTel.trim().isEmpty()) {
      professor.setPTel(newPTel);
    }

    // 주소 업데이트
    if (newPAdd != null && !newPAdd.trim().isEmpty()) {
      professor.setPAdd(newPAdd);
    }

    // 변경된 정보 저장
    professorRepository.save(professor);

    // 성공 메시지 추가
    redirectAttributes.addFlashAttribute("successMessage", "내 정보가 성공적으로 수정되었습니다.");

    return "redirect:/professor/modify"; // 수정 페이지로 리다이렉트
  }
}