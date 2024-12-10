package com.project.lms.controller;

import com.project.lms.entity.Professor;
import com.project.lms.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/professor")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorRepository professorRepository;
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

    // 교수 자신의 정보 업데이트 (주소, 전화번호, 비밀번호)
    @PostMapping("/info")
    public String updateProfessorInfo(
            @Valid @ModelAttribute("professor") Professor updatedProfessor,
            BindingResult bindingResult,
            @RequestParam(required = false) String newPw, // 새 비밀번호
            @RequestParam(required = false) String newPTel, // 새 전화번호
            @RequestParam(required = false) String newPAdd, // 새 주소
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "professor/info";
        }

        String pId = principal.getName();
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

        // 변경된 정보를 저장
        professorRepository.save(professor);

        redirectAttributes.addFlashAttribute("successMessage", "정보가 수정되었습니다.");
        return "redirect:/professor/info";
    }
}