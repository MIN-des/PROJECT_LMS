package com.project.lms.controller.board;

import com.project.lms.dto.board.RegisterFormDTO;
import com.project.lms.entity.Board;
import com.project.lms.service.admin.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/admin/board")
@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardServiceImpl boardServiceImple;
//    private final AllBoardRepository allBoardRepository;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Board> paging = this.boardServiceImple.getList(page);

        int totalPages = paging.getTotalPages();
        int currentBlock = page / 5; // 5개의 페이지 번호로 묶기
        int startPage = currentBlock * 5; // 현재 블록의 시작 페이지
        int endPage = Math.min(startPage + 4, totalPages - 1); // 현재 블록의 마지막 페이지 번호 (최대 페이지 수 제한)

        model.addAttribute("paging", paging);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "admin/board/list";
    }

    //http://localhost:8081/board/detail/10 검색(없는 allBno번호는 나오지 않으니 주의 필요)
    @GetMapping(value = "/detail/{bno}")
    public String detail(Model model, @PathVariable("bno") Long bno) {
        Board board = this.boardServiceImple.getBoard(bno);
        model.addAttribute("board", board);
        return "admin/board/detail";
    }

    //등록
    @GetMapping("/create")
    public String boardCreate(RegisterFormDTO registerFormDto) {
        //model.addAttribute("registerFormDTO", new RegisterFormDTO());// RegisterFormDTO 객체를 모델에 추가
        return "/admin/board/register"; // 폼 페이지 반환
    }

    @PostMapping("/create")
    public String boardCreate(@Valid RegisterFormDTO registerFormDto, BindingResult bindingResult) {  // RegisterFormDTO에 대한 유효성 검사
        // 유효성 검사에서 오류가 있으면 폼을 다시 표시
        if (bindingResult.hasErrors()) {
            return "admin/board/register";
        }
        this.boardServiceImple.create(registerFormDto.getTitle(), registerFormDto.getContent());
        // 게시판 목록으로 리다이렉트
        return "redirect:/admin/board/list";
    }

    @GetMapping("/detail/{bno}/modify")
    public String modifyForm(@PathVariable Long bno, Model model) {
        Board board = boardServiceImple.getBoard(bno);

        RegisterFormDTO registerFormDTO = new RegisterFormDTO();
        registerFormDTO.setTitle(board.getTitle());
        registerFormDTO.setContent(board.getContent());

        model.addAttribute("registerFormDTO", registerFormDTO);
        model.addAttribute("bno", bno);

        return "admin/board/modify";
    }

    @PostMapping("/detail/{bno}/modify")
    public String modify(@PathVariable Long bno, @Valid @ModelAttribute RegisterFormDTO registerFormDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/board/modify";
        }
        boardServiceImple.modify(bno, registerFormDTO.getTitle(), registerFormDTO.getContent());
        return "redirect:/admin/board/detail/{bno}";
    }

    // 게시글 삭제
    @PostMapping("/detail/{bno}/delete")
    public String delete(@PathVariable Long bno) {
        boardServiceImple.delete(bno); // 서비스에서 삭제 로직 호출
        return "redirect:/admin/board/list"; // 게시글 목록 페이지로 리다이렉트
    }


}