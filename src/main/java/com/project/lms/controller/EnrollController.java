package com.project.lms.controller;

import com.project.lms.dto.EnrollDTO;
import com.project.lms.dto.OrderDTO;
import com.project.lms.dto.StudentDTO;
import com.project.lms.service.EnrollService;
import com.project.lms.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/student/enroll")
public class EnrollController {

    private final EnrollService enrollService;
    private final OrderService orderService;

    public EnrollController(EnrollService enrollService, OrderService orderService) {
        this.enrollService = enrollService;
        this.orderService = orderService;
    }

    @GetMapping("/list")
    public String getEnrollPage(Model model, Principal principal) {
        String sId = principal.getName();
        System.out.println("Logged-in student ID: " + sId);

        if (sId == null) {
            throw new IllegalArgumentException("로그인 정보가 없습니다.");
        }

        EnrollDTO enrollDTO = enrollService.createEnrollOnLogin(sId);

        model.addAttribute("courses", enrollDTO.getEnrollCourses());
        model.addAttribute("enroll", enrollDTO);
        return "enroll/list";
    }

    @PostMapping("/add")
    public String addCourseToEnroll(@RequestParam Long cId,
                                    @RequestParam String sId,
                                    RedirectAttributes redirectAttributes,
                                    Principal principal) {
        try {

            sId = principal.getName();

            // 예비수강신청 로직 호출
            enrollService.addCourseToEnroll(sId, cId);
            redirectAttributes.addFlashAttribute("successMessage", "강의가 추가되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/student/courses/list";
    }

    @PostMapping("/delete")
    public String deleteCourseFromEnroll(@RequestParam Long cId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            String sId = principal.getName();
            enrollService.removeCourseFromEnroll(sId, cId);
            redirectAttributes.addFlashAttribute("successMessage", "강의가 삭제 되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "강의가 삭제 되지 않았습니다. 다시 시도해주세요");
        }
        return "redirect:/student/enroll/list";
    }

    @PostMapping("/sub")
    public String subCourse(@RequestParam Long cId, Principal principal,
                            RedirectAttributes redirectAttributes) {
        try {
            // 현재 로그인한 학생 ID 가져오기
            String sId = principal.getName();

            // 수강 신청 처리
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setCId(cId);

            // Student 정보를 StudentDTO로 설정
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setSId(sId);
            orderDTO.setStudent(studentDTO);

            // OrderService를 통해 주문 생성
            orderService.createOrder(orderDTO);

            // 성공적으로 수강신청 완료 시, 예비수강신청 리스트에서 해당 강의 제거
            enrollService.removeCourseFromEnroll(sId, cId);

            // 성공 메시지 설정
            redirectAttributes.addFlashAttribute("successMessage", "수강 신청이 완료되었습니다!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미 신청한 강의입니다.");
        }

        // 등록된 강의 목록 페이지로 리다이렉트
        return "redirect:/student/enroll/list";
    }


}
