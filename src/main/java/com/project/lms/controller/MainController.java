package com.project.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/")
public class MainController {

    @GetMapping("/hello")  // /hello 경로로 접근 시 hello.html을 반환
    public String hello() {
        return "hello";  // hello.html 파일을 찾아 반환
    }
    @GetMapping("/")
    public String root() {
        return "redirect:/hello";
    }
}
