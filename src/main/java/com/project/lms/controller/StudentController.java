package com.project.lms.controller;

import com.project.lms.dto.CourseDTO;
import com.project.lms.dto.StudentDTO;
import com.project.lms.entity.Student;
import com.project.lms.service.CourseService;
import com.project.lms.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final CourseService courseService; // 강의 서비스
//    private final EnrollmentService enrollmentService; // 수강 신청 서비스

    // 강의 목록 조회
    @GetMapping("/course/list")
    public String listCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String searchQuery,
            Model model) {
        // 강의 목록 조회 및 검색
        Page<CourseDTO> coursePage;

        // 검색 조건에 따른 데이터 검색
        if ("id".equals(searchType)) {
            coursePage = courseService.searchCoursesById(
                    Long.parseLong(searchQuery), PageRequest.of(page, 10));
        } else if ("name".equals(searchType)) {
            coursePage = courseService.searchCoursesByName(
                    searchQuery, PageRequest.of(page, 10));
        } else if ("status".equals(searchType)) {
            coursePage = courseService.searchCoursesByRestStatus(
                    searchQuery, PageRequest.of(page, 10));
        } else if ("createdBy".equals(searchType)) {
            coursePage = courseService.searchCourseByCreatedBy(
                    searchQuery, PageRequest.of(page, 10));
        } else {
            // 검색 조건이 없을 경우 기본 전체 목록 조회
            coursePage = courseService.getAllCourses(PageRequest.of(page, 10));
        }

        model.addAttribute("page", coursePage);
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchQuery", searchQuery);

        return "/student/courseList";
    }

    @GetMapping("/info/{sId}")
    public String getStudentById(@PathVariable String sId, Model model) {
        // 서비스 호출하여 학생 정보 조회
        Student student = studentService.getStudentInfo(sId);
        model.addAttribute("student", student);
        return "student/info";
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
