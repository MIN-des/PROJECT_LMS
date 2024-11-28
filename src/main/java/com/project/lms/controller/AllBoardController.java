/*
package com.project.lms.controller;

import com.project.lms.dto.AllBoardDTO;
import com.project.lms.dto.PageRequestDTO;
import com.project.lms.dto.PageResponseDTO;
import com.project.lms.service.AllBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/all/board")
// 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성(@Autowired 사용하지 않고 의존성 주입)
@RequiredArgsConstructor
public class AllBoardController {

    private final AllBoardService allBoardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<AllBoardDTO> responseDTO = allBoardService.list(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
    }
}*/
