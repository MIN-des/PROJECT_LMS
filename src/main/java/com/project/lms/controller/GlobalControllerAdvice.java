package com.project.lms.controller;

import com.project.lms.entity.Admin;
import com.project.lms.entity.Professor;
import com.project.lms.entity.Student;
import com.project.lms.repository.AdminRepository;
import com.project.lms.repository.ProfessorRepository;
import com.project.lms.repository.StudentRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.Optional;

// 전역설정
@ControllerAdvice
public class GlobalControllerAdvice {

	private final ProfessorRepository professorRepository;
	private final StudentRepository studentRepository;
	private final AdminRepository adminRepository;

	public GlobalControllerAdvice(ProfessorRepository professorRepository, StudentRepository studentRepository, AdminRepository adminRepository) {
		this.professorRepository = professorRepository;
		this.studentRepository = studentRepository;
		this.adminRepository = adminRepository;
	}

	@ModelAttribute
	public void addGlobalAttributes(Model model, Principal principal) {
		if (principal != null) {
			String userId = principal.getName();
			model.addAttribute("userId", userId);

			// 사용자 정보 조회 및 추가
			Optional<Professor> professor = professorRepository.findById(userId);
			professor.ifPresent(p -> model.addAttribute("userName", p.getPName()));

			Optional<Student> student = studentRepository.findById(userId);
			student.ifPresent(p -> model.addAttribute("userName", p.getSName()));

			Optional<Admin> admin = adminRepository.findById(userId);
			admin.ifPresent(p -> model.addAttribute("userName", p.getAName()));
		}
	}
}
