package com.project.lms.controller;

import com.project.lms.dto.CourseDTO;
import com.project.lms.dto.CourseSearchDTO;
import com.project.lms.entity.Course;
import com.project.lms.repository.CourseRepository;
import com.project.lms.repository.CourseRepositoryCustomImpl;
import com.project.lms.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController { // 강의 조회(권한 전체), 생성/수정/삭제(교수)

    private final CourseService courseService;
    private final CourseRepositoryCustomImpl courseRepositoryCustom;
    private final CourseRepository courseRepository;

    // 강의 등록 페이지
    @GetMapping("/create")
    public String courseCreate(Model model) {
        model.addAttribute("courseDTO", new CourseDTO());
        return "course/create"; // 강의 등록 페이지
    }

    // 강의 등록 데이터 저장
    @PostMapping("/create")
    public String courseCreate(@Valid CourseDTO courseDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "course/create";
        }
        try {
            courseService.createCourse(courseDTO);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "강의 등록 중 오류가 발생하였습니다.");
            return "course/create";
        }
        return "redirect:/courses/professor/list"; // 등록 후 강의 목록 페이지로 이동
    }

    // 교수 강의 목록 조회
    @GetMapping(value = {"/professor/list", "/professor/list/{page}"})
    public String courseListAll(CourseSearchDTO searchDTO, @PathVariable("page") Optional<Integer> page, Model model) {
        int currentPage = page.orElse(0); // 페이지 기본값: 0

        Pageable pageable = PageRequest.of(currentPage, 5); // 페이지 크기: 5
        Page<Course> courses = courseRepositoryCustom.getProfessorCoursePage(searchDTO, pageable);

        model.addAttribute("courses", courses);
        model.addAttribute("searchDTO", searchDTO);
        model.addAttribute("maxPage", 5);

        return "course/list"; // 강의 목록 뷰
    }

    // 강의 상세 조회
    @GetMapping("/detail/{cId}")
    public String courseDetail(@PathVariable("cId") Long cId, Model model) {
        try {
            CourseDTO courseDTO = courseService.getCourse(cId);
            model.addAttribute("courseDTO", courseDTO);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "존재하지 않는 강의입니다.");
            return "redirect:/courses/professor/list";
        }
        return "course/detail"; // 강의 상세 페이지
    }

    // 강의 수정
    @PostMapping("/update/{cId}")
    public String courseUpdate(@Valid CourseDTO courseDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "course/detail";
        }
        try {
            courseService.updateCourse(courseDTO);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "강의 수정 중 오류가 발생하였습니다.");
            return "course/detail";
        }
        return "redirect:/courses/professor/list";
    }

    // 강의 삭제
    @PostMapping("/delete/{cId}")
    public String courseDelete(@PathVariable("cId") Long cId, Model model) {
        try {
            courseRepository.findById(cId);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "강의 삭제 중 오류가 발생하였습니다.");
            return "redirect:/courses/professor/list";
        }
        return "redirect:/courses/professor/list";
    }
}