package com.project.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

  @GetMapping("/login")
  public String loginPage() {
    return "login"; // login.html 반환
  }

  @GetMapping("/login/error")
  public String loginError(Model model) {
    model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");

    return "login";
  }
}
