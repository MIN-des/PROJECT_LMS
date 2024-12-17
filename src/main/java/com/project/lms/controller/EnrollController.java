package com.project.lms.controller;//package com.project.lms.controller;
//
//import com.project.lms.dto.EnrollCourseDTO;
//import com.project.lms.service.EnrollService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/student/enroll")
//@RequiredArgsConstructor
//public class EnrollController {
//
//  private final EnrollService enrollService;
//
//  /**
//   * 로그인한 학생의 장바구니 페이지를 반환합니다.
//   *
//   * @param model Spring MVC 모델 객체
//   * @return 장바구니 페이지 템플릿 이름
//   */
//  @GetMapping("/cart")
//  public String viewCart(Model model) {
//    // 현재 인증된 사용자 정보 가져오기
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    String studentId = authentication.getName(); // 사용자 ID (로그인 시 사용한 ID로 간주)
//
//    // 장바구니 데이터 조회
//    List<EnrollCourseDTO> enrollCourses = enrollService.getEnrollCourses(studentId);
//
//    // 모델에 데이터 추가
//    model.addAttribute("enrollCourses", enrollCourses);
//
//    // 뷰 템플릿 이름 반환
//    return "student/cart";
//  }
//
//  @PostMapping("/cart/confirm")
//  public String confirmEnrollment(Model model) {
//    // 현재 로그인된 학생 정보 가져오기
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    String studentId = authentication.getName();
//
//    try {
//      enrollService.confirmEnrollment(studentId);
//      model.addAttribute("message", "강의 신청이 완료되었습니다.");
//    } catch (Exception e) {
//      model.addAttribute("error", e.getMessage());
//    }
//
//    // 장바구니 페이지로 리다이렉트
//    return "redirect:/student/enroll/cart";
//  }
//}