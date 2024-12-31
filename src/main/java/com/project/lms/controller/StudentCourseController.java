package com.project.lms.controller;

import com.project.lms.dto.CourseDTO;
import com.project.lms.dto.OrderDTO;
import com.project.lms.dto.StudentDTO;
import com.project.lms.service.CourseService;
import com.project.lms.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/student/courses")
@RequiredArgsConstructor
public class StudentCourseController { // 교수가 등록한 강의 목록 조회 및 수강 신청, 수강 내역 조회까지

    private final CourseService courseService;
    private final OrderService orderService;

    @GetMapping("/list")
    public String getStudentCourses(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(required = false) String searchType,
                                    @RequestParam(required = false) String searchQuery,
                                    Model model) {

        Page<CourseDTO> coursePage = Page.empty(); // init

        // JPA 페이징은 0부터 시작하므로, page 값이 1 미만이면 0으로 설정
        int pageIndex = (page > 0) ? page - 1 : 0;

        // 기본 정렬 조건: 최신 등록 순
        Pageable pageable = PageRequest.of(pageIndex, 10, Sort.by(Sort.Direction.DESC, "regTime"));

        try {
            if (searchType != null && !searchType.isBlank() && searchQuery != null && !searchQuery.isBlank()) {
                switch (searchType) {
                    case "id":
                        if (!searchQuery.matches("\\d+")) {
                            throw new IllegalArgumentException("강의 ID는 숫자만 입력 가능합니다.");
                        }

                        coursePage = courseService.searchCoursesById(Long.parseLong(searchQuery), pageable);
                        break;
                    case "name":
                        coursePage = courseService.searchCoursesByName(searchQuery, pageable);
                        break;
                    case "pId":
                        coursePage = courseService.searchCourseByProfessor_pId(searchQuery, pageable);
                        break;
                    case "pName":
                        coursePage = courseService.searchCourseByProfessor_pName(searchQuery, pageable);
                        break;
                    default:
                        throw new IllegalArgumentException("유효하지 않은 검색 유형입니다.");
                }
            } else {
                coursePage = courseService.getAllCourses(pageable);
            }

            if (coursePage.getTotalElements() == 0) {
                model.addAttribute("errorMessage", "검색 결과가 없습니다.");
            }

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        // 페이징 정보 계산
        int totalPages = coursePage.getTotalPages();
        int pageSize = 5; // 표시할 페이지 버튼 수
        int currentBlock = (page - 1) / pageSize; // 현재 페이지가 속한 블록 계산
        int startPage = (currentBlock * pageSize) + 1;
        int endPage = Math.min(startPage + pageSize - 1, totalPages); // 마지막 페이지가 전체 페이지 수를 넘지 않도록 설정

        // 모델에 추가
        model.addAttribute("page", coursePage);
        model.addAttribute("currentPage", totalPages > 0 ? page : 0);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
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
