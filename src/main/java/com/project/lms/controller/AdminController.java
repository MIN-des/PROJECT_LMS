package com.project.lms.controller;

import com.project.lms.constant.Dept;
import com.project.lms.dto.ProfessorDTO;
import com.project.lms.dto.StudentDTO;
import com.project.lms.service.ProfessorService;
import com.project.lms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private ProfessorService professorService;

	// 메인 페이지
	@GetMapping
	public String adminHome() {
		return "admin/home";
	}


	// 학생 생성 폼 페이지
	@GetMapping("/students/create")
	public String createStudentForm(Model model) {
		model.addAttribute("student", new StudentDTO());
		return "admin/student/create";
	}

	// 학생 생성 처리
	@PostMapping("/students/create")
	public String createStudent(@ModelAttribute StudentDTO studentDTO, Model model) {
		try {
			studentService.createStudent(studentDTO);
			return "redirect:/admin/students?page=0";
		} catch (IllegalArgumentException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("student", studentDTO); // 입력된 데이터 유지
			return "admin/student/create";
		}
	}
	// 교수 생성 폼 페이지
	@GetMapping("/professors/create")
	public String createProfessorForm(Model model) {
		model.addAttribute("professor", new ProfessorDTO());
		return "admin/professor/create";
	}

	// 교수 생성 처리
	@PostMapping("/professors/create")
	public String createProfessor(@ModelAttribute ProfessorDTO professorDTO, Model model) {
		try {
			professorService.createProfessor(professorDTO);
			return "redirect:/admin/professors?page=0";
		} catch (IllegalArgumentException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("professor", professorDTO);
			return "admin/professor/create";
		}
	}

	// 학생 리스트
	@GetMapping("/students")
	public String listStudents(@RequestParam(required = false) String searchType,
														 @RequestParam(required = false) String keyword,
														 @RequestParam(defaultValue = "sId") String sortField, // 정렬 필드
														 @RequestParam(defaultValue = "asc") String sortDir,  // 정렬 방향
														 @RequestParam(required = false) Boolean isSorted,   // 정렬 여부 플래그
														 @RequestParam(value = "page", defaultValue = "0") int page,
														 Pageable pageable, Model model) {

		// 정렬 생성
		Sort sort = Sort.by(sortField);
		if ("desc".equalsIgnoreCase(sortDir)) {
			sort = sort.descending();
		}
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

		Page<StudentDTO> students;
		String errorMessage = null; // errorMessage 변수를 선언하고 초기화

		try {
			// 검색 조건에 따라 결과를 가져옴
			if ("id".equalsIgnoreCase(searchType)) {
				students = studentService.searchStudentsById(keyword, pageable);
			} else if ("name".equalsIgnoreCase(searchType)) {
				students = studentService.searchStudentsByName(keyword, pageable);
			} else if ("dept".equalsIgnoreCase(searchType)) {
				if (keyword == null || keyword.isEmpty()) {
					students = studentService.getAllStudents(pageable);
				} else {
//					Dept.valueOf(keyword.toUpperCase());
					students = studentService.searchStudentsByDept(keyword, pageable);
				}
			} else {
				students = studentService.getAllStudents(pageable);
			}
		} catch (IllegalArgumentException e) {
			students = studentService.getAllStudents(pageable);
			model.addAttribute("errorMessage", "유효하지 않은 학부 값입니다. 올바른 학부를 입력해주세요.");
		}

		// 페이지네이션 로직
		int currentPage = students.getNumber() + 1; // 현재 페이지 (1부터 시작)
		int totalPages = students.getTotalPages();
		int groupSize = 10; // 페이지 그룹 크기
		int currentGroup = (currentPage - 1) / groupSize; // 현재 그룹 계산
		int startPage = currentGroup * groupSize + 1; // 그룹 시작 페이지
		int endPage = Math.min(startPage + groupSize - 1, totalPages); // 그룹 종료 페이지

		// 모델에 데이터 추가
		model.addAttribute("students", students);
		model.addAttribute("searchType", searchType != null ? searchType : "");
		model.addAttribute("keyword", keyword != null ? keyword : "");
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("isSorted", isSorted != null && isSorted); // 정렬 여부 전달
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("groupSize", groupSize);
		model.addAttribute("page", page);

		return "admin/student/list";
	}


	// 학생 상세정보
	@GetMapping("/students/{id}")
	public String getStudent(@PathVariable String id, Model model,
													 @RequestParam(required = false) String searchType,
													 @RequestParam(required = false) String keyword,
													 @RequestParam(required = false) Integer page) {
		StudentDTO student = studentService.getStudentById(id)
				.orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));
		model.addAttribute("student", student);
		model.addAttribute("searchType", searchType);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		return "admin/student/detail";
	}

	// 학생 수정
	@PostMapping("/students/{id}/edit")
	@ExceptionHandler
	public String updateStudent(@PathVariable String id, @ModelAttribute StudentDTO studentDTO,
															@RequestParam(required = false) String searchType,
															@RequestParam(required = false) String keyword,
															@RequestParam(required = false, defaultValue = "0") Integer page,
															Model model) {

		try {
			studentService.updateStudent(id, studentDTO);
			return "redirect:/admin/students?page=" + (page - 1) +
					(searchType != null ? "&searchType=" + searchType : "") +
					(keyword != null ? "&keyword=" + keyword : "");
		} catch (IllegalArgumentException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("student", studentDTO);
			model.addAttribute("page", page);
			model.addAttribute("searchType", searchType);
			model.addAttribute("keyword", keyword);
			return "admin/student/detail";
		}
	}

	// 학생 삭제
	@PostMapping("/students/{id}/delete")
	public String deleteStudent(@PathVariable String id,
															@RequestParam(required = false) String searchType,
															@RequestParam(required = false) String keyword,
															@RequestParam(required = false, defaultValue = "0") Integer page) {
		studentService.deleteStudent(id);
		return "redirect:/admin/students?page=" + (page - 1) +
				(searchType != null ? "&searchType=" + searchType : "") +
				(keyword != null ? "&keyword=" + keyword : "");
	}

	// 교수 리스트
	@GetMapping("/professors")
	public String listProfessors(@RequestParam(required = false) String searchType,
															 @RequestParam(required = false) String keyword,
															 @RequestParam(defaultValue = "pId") String sortField, // 정렬 필드
															 @RequestParam(defaultValue = "asc") String sortDir,  // 정렬 방향
															 @RequestParam(required = false) Boolean isSorted,   // 정렬 여부 플래그
															 Pageable pageable, Model model) {

		// 정렬 생성
		Sort sort = Sort.by(sortField);
		if ("desc".equalsIgnoreCase(sortDir)) {
			sort = sort.descending();
		}
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

		Page<ProfessorDTO> professors;
		String errorMessage = null; // errorMessage 변수를 선언하고 초기화
		try {
			if ("id".equalsIgnoreCase(searchType)) {
				professors = professorService.searchProfessorsById(keyword, pageable);
			} else if ("name".equalsIgnoreCase(searchType)) {
				professors = professorService.searchProfessorsByName(keyword, pageable);
			} else if ("dept".equalsIgnoreCase(searchType)) {
				if (keyword == null || keyword.isEmpty()) {
					professors = professorService.getAllProfessors(pageable);
				} else {
					professors = professorService.searchProfessorsByDept(keyword, pageable);
				}
			} else {
				professors = professorService.getAllProfessors(pageable);
			}
		} catch (IllegalArgumentException e) {
			professors = professorService.getAllProfessors(pageable);
			model.addAttribute("errorMessage", "유효하지 않은 학부 값입니다. 올바른 학부를 입력해주세요.");
		}

		// 페이지네이션 로직
		int currentPage = professors.getNumber() + 1; // 현재 페이지 (1부터 시작)
		int totalPages = professors.getTotalPages();
		int groupSize = 10; // 페이지 그룹 크기
		int currentGroup = (currentPage - 1) / groupSize; // 현재 그룹 계산
		int startPage = currentGroup * groupSize + 1; // 그룹 시작 페이지
		int endPage = Math.min(startPage + groupSize - 1, totalPages); // 그룹 종료 페이지

		model.addAttribute("professors", professors);
		model.addAttribute("searchType", searchType != null ? searchType : "");
		model.addAttribute("keyword", keyword != null ? keyword : "");
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("isSorted", isSorted != null && isSorted); // 정렬 여부 전달
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("groupSize", groupSize);
		return "admin/professor/list";
	}

	// 교수 상세정보
	@GetMapping("/professors/{id}")
	public String getProfessor(@PathVariable String id, Model model,
														 @RequestParam(required = false) String searchType,
														 @RequestParam(required = false) String keyword,
														 @RequestParam(required = false) Integer page) {
		ProfessorDTO professor = professorService.getProfessorById(id)
				.orElseThrow(() -> new IllegalArgumentException("교수를 찾을 수 없습니다."));
		model.addAttribute("professor", professor);
		model.addAttribute("searchType", searchType);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		return "admin/professor/detail";
	}

	// 교수 수정
	@PostMapping("/professors/{id}/edit")
	public String updateProfessor(@PathVariable String id, @ModelAttribute ProfessorDTO professorDTO,
																@RequestParam(required = false) String searchType,
																@RequestParam(required = false) String keyword,
																@RequestParam(required = false, defaultValue = "0") Integer page,
																Model model) {
		try {
			professorService.updateProfessor(id, professorDTO);
			return "redirect:/admin/professors?page=" + (page - 1) +
					(searchType != null ? "&searchType=" + searchType : "") +
					(keyword != null ? "&keyword=" + keyword : "");
		} catch (IllegalArgumentException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("professor", professorDTO);
			model.addAttribute("page", page);
			model.addAttribute("searchType", searchType);
			model.addAttribute("keyword", keyword);
			return "admin/professor/detail";
		}
	}

	// 교수 삭제
	@PostMapping("/professors/{id}/delete")
	public String deleteProfessor(@PathVariable String id,
																@RequestParam(required = false, defaultValue = "") String searchType,
																@RequestParam(required = false, defaultValue = "") String keyword,
																@RequestParam(required = false, defaultValue = "0") Integer page) {
		professorService.deleteProfessor(id);
		return "redirect:/admin/professors?page=" + (page - 1) +
				(searchType != null ? "&searchType=" + searchType : "") +
				(keyword != null ? "&keyword=" + keyword : "");
	}
}
