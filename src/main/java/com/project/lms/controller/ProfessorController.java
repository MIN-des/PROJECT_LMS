package com.project.lms.controller;

import com.project.lms.entity.Professor;
import com.project.lms.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String updateProfessorInfo(@Valid @ModelAttribute("professor") Professor updatedProfessor,
                                      BindingResult bindingResult,
                                      Principal principal, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "professor/info"; // 에러 발생 시 원래 페이지로
        }

        String pId = principal.getName(); // 로그인된 교수의 ID 가져오기
        Professor professor = professorRepository.findById(pId)
                .orElseThrow(() -> new IllegalArgumentException("Professor not found for ID: " + pId));

        // 비밀번호가 업데이트되었을 경우 암호화 처리
        if (updatedProfessor.getPPw() != null && !updatedProfessor.getPPw().isEmpty()) {
            professor.setPPw(passwordEncoder.encode(updatedProfessor.getPPw()));
        }

        // 업데이트 가능한 필드 업데이트
        professor.setPTel(updatedProfessor.getPTel());
        professor.setPAdd(updatedProfessor.getPAdd());

        professorRepository.save(professor);

        redirectAttributes.addFlashAttribute("successMessage", "정보가 수정되었습니다.");

        return "redirect:/professor/info"; // 리다이렉트
    }
}