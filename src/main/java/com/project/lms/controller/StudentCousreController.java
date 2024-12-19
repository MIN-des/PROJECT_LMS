package com.project.lms.controller;

import com.project.lms.dto.CourseDTO;
import com.project.lms.dto.OrderDTO;
import com.project.lms.dto.StudentDTO;
import com.project.lms.service.CourseService;
import com.project.lms.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/student/courses")
@RequiredArgsConstructor
public class StudentCousreController { // 교수가 등록한 강의 목록 조회 및 수강 신청, 수강 내역 조회까지

    private final CourseService courseService;
    private final OrderService orderService;

    @GetMapping("/list")
    public String getStudentCourses(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(required = false) String searchType,
                                    @RequestParam(required = false) String searchQuery,
                                    Model model) {

        Page<CourseDTO> coursePage;

        // 검색 조건이 없는 경우 전체 목록 조회
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            coursePage = courseService.getAllCourses(PageRequest.of(page, 10));
        } else {
            // 검색 조건에 따른 데이터 검색
            if ("id".equals(searchType)) {
                coursePage = courseService.searchCoursesById(
                        Long.parseLong(searchQuery), PageRequest.of(page, 10));
            } else if ("name".equals(searchType)) {
                coursePage = courseService.searchCoursesByName(
                        searchQuery, PageRequest.of(page, 10));
            } else if ("pId".equals(searchType)) {
                coursePage = courseService.searchCourseByProfessor_pId(
                        searchQuery, PageRequest.of(page, 10)); // 교수 아이디로 검색
            } else {
                coursePage = courseService.getAllCourses(PageRequest.of(page, 10));
            }

            // 검색 결과가 없는 경우 메시지 추가
            if (coursePage.isEmpty()) {
                model.addAttribute("errorMessage", "검색 결과가 없습니다.");
            }
        }

        model.addAttribute("page", coursePage);
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchQuery", searchQuery);

        return "student/courseList"; // 등록된 강의 목록 조회 페이지
    }

    // 수강신청
    @PostMapping("/sub")
    public String subCourse(@RequestParam Long courseId, Principal principal,
                            RedirectAttributes redirectAttributes) {
        try {
            // 현재 로그인한 학생 ID 가져오기
            String studentId = principal.getName();

            // 수강 신청 처리
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setCId(courseId);

            // Student 정보를 StudentDTO로 설정
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setSId(studentId);
            orderDTO.setStudent(studentDTO);

            // OrderService를 통해 주문 생성
            orderService.createOrder(orderDTO);

            // 성공 메시지 설정
            redirectAttributes.addFlashAttribute("successMessage", "수강 신청이 완료되었습니다!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미 신청한 강의입니다.");
        }

        // 등록된 강의 목록 페이지로 리다이렉트
        return "redirect:/student/courses/list";
    }

    @GetMapping("/subList")
    public String getStudentSubList(Model model, Principal principal) {
        String sId = principal.getName();

        // 학생 ID로 본인 신청 내역 조회 (페이징 제거)
        List<OrderDTO> orderList = orderService.getOrdersByStudent_sId(sId);

        if (orderList.isEmpty()) {
            model.addAttribute("noOrdersMessage", "신청 내역이 없습니다.");
        } else {
            model.addAttribute("orderList", orderList);
        }

        return "student/subList"; // subList.html 템플릿 반환
    }

    @PostMapping("/cancel/{oId}")
    public String cancelOrder(@PathVariable("oId") Long oId, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(oId);
            redirectAttributes.addFlashAttribute("successMessage", "신청이 취소되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "신청 취소 중 오류가 발생했습니다.");
        }

        return "redirect:/student/courses/subList";
    }
}
