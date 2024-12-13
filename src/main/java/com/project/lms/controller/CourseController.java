package com.project.lms.controller;

import com.project.lms.dto.CourseDTO;
import com.project.lms.dto.OrderDTO;
import com.project.lms.entity.Course;
import com.project.lms.entity.Order;
import com.project.lms.repository.CourseRepository;
import com.project.lms.service.CourseService;
import com.project.lms.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Controller
@RequestMapping("/professor/courses")
@RequiredArgsConstructor
public class CourseController { // 강의 조회(권한 전체), 생성/수정/삭제(교수)

    private final CourseRepository courseRepository;
    private final OrderService orderService;
    private final CourseService courseService;

    @GetMapping("/create")
    public String createCourseForm(Model model) {
        CourseDTO courseDTO = new CourseDTO();

        // 현재 로그인한 사용자 ID 가져오기
        String loggedInUserId = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "anonymous"; // 인증되지 않은 사용자 처리

        courseDTO.setPId(loggedInUserId);
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

            courseDTO.setPId(loggedInUserId);

            // 강의 생성 호출
            courseService.createCourse(courseDTO);

            // 생성 완료 후 목록으로 이동
            return "redirect:/professor/courses/list";

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
        } /*else if ("status".equals(searchType)) {
            coursePage = courseService.searchCoursesByRestStatus(
                    searchQuery, PageRequest.of(page, 10));
        }*/ else if ("pId".equals(searchType)) {
            coursePage = courseService.searchCourseByProfessor_pId(
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
    public String updateCourseForm(@PathVariable Long cId, Model model) {
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
            CourseDTO existingCourse = courseService.getCourseById(cId)
                    .orElseThrow(() -> new EntityNotFoundException("강의를 찾을 수 없습니다: ID " + cId));

            // pId와 현재 로그인한 사용자 비교
            if (!existingCourse.getPId().equals(currentUser)) {
                throw new AccessDeniedException("본인이 등록한 강의만 수정할 수 있습니다.");
            }

            // 강의 ID 불일치 확인
            if (!cId.equals(courseDTO.getCId())) {
                throw new IllegalArgumentException("ID 불일치: 요청된 ID와 강의 데이터 ID가 다릅니다.");
            }

            // 강의 업데이트 호출
            courseService.updateCourse(cId, courseDTO);

            return "redirect:/professor/courses/list";

        } catch (AccessDeniedException | IllegalArgumentException | EntityNotFoundException e) {
            // 사용자가 발생시킨 오류 처리
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("course", courseDTO);

            return "course/update";
        } catch (Exception e) {
            // 예상치 못한 오류 처리
            model.addAttribute("errorMsg", "강의 수정 중 오류가 발생했습니다. 다시 시도해주세요.");
            model.addAttribute("course", courseDTO);

            return "course/update";
        }
    }

    // 강의 삭제
    @PostMapping("/delete")
    public String deleteCourse(@RequestParam Long cId,
                               RedirectAttributes redirectAttributes,
                               Principal principal) {

        String professorId = principal.getName(); // 현재 로그인한 교수의 ID
        Course course = courseRepository.findById(cId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다."));

        // 강의를 등록한 교수인지 확인
        if (!course.getCreatedBy().equals(professorId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "본인이 등록한 강의만 삭제할 수 있습니다.");

            return "redirect:/professor/courses/list"; // 목록 페이지로 리다이렉트
        }

        courseRepository.delete(course);
        redirectAttributes.addFlashAttribute("successMessage", "강의가 삭제되었습니다.");

        return "redirect:/professor/courses/list"; // 목록 페이지로 리다이렉트
    }

    // 내 강의 조회
    @GetMapping("/myCourses")
    public String getMyCourses(Principal principal, Model model) {
        String pId = principal.getName(); // 로그인된 교수의 ID 가져오기
        List<Course> courses = courseService.getCoursesByProfessorId(pId);
        model.addAttribute("courses", courses);

        return "professor/myCourses";
    }

    // 내 강의 수강 신청한 학생 조회
    @GetMapping({"/studentList", "/studentList/{cId}"})
    public String getMyCoursesStudentList(@PathVariable Long cId, Model model) {
        // 특정 강의에 대한 신청 목록 조회
        List<Order> orders = orderService.getOrdersByCourse_cId(cId);

        // 데이터 모델에 추가
        model.addAttribute("orders", orders);
        model.addAttribute("cId", cId);

        return "professor/studentList";
    }

    @PostMapping("/studentList/score")
    public String updateStudentGrade(@Valid @ModelAttribute OrderDTO orderDTO,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors() || orderDTO.getCId() == null || orderDTO.getOId() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "필수 데이터가 누락되었거나 입력값 검증에 실패했습니다.");
            return "redirect:/professor/courses/studentList/" + orderDTO.getCId();
        }

        try {
            orderService.updateScore(orderDTO.getOId(), orderDTO.getScore());
            redirectAttributes.addFlashAttribute("successMessage", "성적이 저장되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "성적 저장 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "redirect:/professor/courses/studentList/" + orderDTO.getCId();
    }

}
