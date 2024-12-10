package com.project.lms.controller;

import com.project.lms.dto.CourseDTO;
import com.project.lms.repository.CourseRepository;
import com.project.lms.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/professor/courses")
@RequiredArgsConstructor
public class CourseController { // 강의 조회(권한 전체), 생성/수정/삭제(교수)

    private final CourseRepository courseRepository;
    private final CourseService courseService;

    @GetMapping("/create")
    public String createCourseForm(Model model) {
        CourseDTO courseDTO = new CourseDTO();

        // 현재 로그인한 사용자 ID 가져오기
        String loggedInUserId = SecurityContextHolder.getContext().getAuthentication() != null
            ? SecurityContextHolder.getContext().getAuthentication().getName()
            : "anonymous"; // 인증되지 않은 사용자 처리

        courseDTO.setCreatedBy(loggedInUserId);
        model.addAttribute("course", courseDTO);

        return "course/create"; // 강의 생성 템플릿으로 이동
    }

    @PostMapping("/create")
    public String createCourse(@ModelAttribute CourseDTO courseDTO, Model model) {
        try {
            // 현재 로그인한 사용자 ID 설정
            String loggedInUserId = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "anonymous"; // 인증되지 않은 사용자 처리

            courseDTO.setCreatedBy(loggedInUserId);

            // 강의 생성 호출
            courseService.createCourse(courseDTO);

            // 생성 완료 후 목록으로 이동
            return "redirect:/professor/courses/list?page=0";

        } catch (IllegalArgumentException e) {
            // 잘못된 입력 처리
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("course", courseDTO);

            return "course/create"; // 강의 생성 페이지에 머무름
        } catch (Exception e) {
            // 예상하지 못한 오류 처리
            model.addAttribute("errorMsg", "강의를 생성하는 중 문제가 발생했습니다. 다시 시도해주세요.");
            model.addAttribute("course", courseDTO);

            return "course/create";
        }
    }

    // 강의 목록 조회 (페이징 포함)
    @GetMapping("/list")
    public String listCourses(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) String searchType,
        @RequestParam(required = false) String searchQuery,
        Model model) {

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

        return "course/list";
    }

    // 강의 수정 폼
    @GetMapping("/update/{cId}")
    public String updateCourse(@PathVariable Long cId, Model model) {
        Optional<CourseDTO> courseOptional = courseService.getCourseById(cId);

        if (courseOptional.isPresent()) {
            model.addAttribute("course", courseOptional.get());
        } else {
            model.addAttribute("errorMsg", "Course not found for the provided ID: " + cId);

            return "redirect:/professor/courses/list";
        }

        return "course/update";
    }

    // 강의 수정
    @PostMapping("/update/{cId}")
    public String updateCourse(@PathVariable Long cId, @ModelAttribute CourseDTO courseDTO, Model model) {
        try {
            // 현재 로그인한 사용자 가져오기
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

            // 해당 강의 가져오기
            Optional<CourseDTO> existingCourse = courseService.getCourseById(cId);

            if (existingCourse.isEmpty()) {
                throw new EntityNotFoundException("Course not found with ID: " + cId);
            }

            // createdBy와 현재 로그인한 사용자 비교
            if (!existingCourse.get().getCreatedBy().equals(currentUser)) {
                throw new AccessDeniedException("You do not have permission to update this course.");
            }

            // 강의 ID 불일치 확인
            if (!cId.equals(courseDTO.getCId())) {
                throw new IllegalArgumentException("ID mismatch: PathVariable ID and CourseDTO ID are different.");
            }

            courseService.updateCourse(cId, courseDTO);
            return "redirect:/professor/courses/list?page=0";

        } catch (AccessDeniedException | IllegalArgumentException | EntityNotFoundException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("course", courseDTO);

            return "course/update";
        }
    }

    // 강의 삭제
    @PostMapping("/delete")
    public String deleteCourse(@RequestParam Long cId, RedirectAttributes redirectAttributes, Principal principal) {
        String currentUserId = principal.getName(); // 현재 로그인한 사용자 ID 가져오기
        String createdBy = courseService.findCreatedBy(cId); // 강의를 생성한 사용자 ID 가져오기

        if (!currentUserId.equals(createdBy)) {
            throw new AccessDeniedException("본인이 등록한 강의만 삭제 가능합니다.");
        }

        courseService.deleteCourse(cId); // 강의 삭제
        redirectAttributes.addFlashAttribute("result", "deleted");

        return "redirect:/professor/courses/list";
    }
}
