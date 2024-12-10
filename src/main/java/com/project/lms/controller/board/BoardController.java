package com.project.lms.controller.board;

import com.project.lms.dto.board.RegisterFormDTO;
import com.project.lms.entity.Board;
import com.project.lms.entity.Files;
import com.project.lms.service.admin.BoardServiceImpl;
import com.project.lms.service.admin.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequestMapping("/board")
@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardServiceImpl boardServiceImpl;
    private final FileServiceImpl fileService;

    @Value("${file.upload-dir}")
    private String uploadDir;  // 파일 업로드 경로 설정 (application.properties에서 설정)

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page) {
        Page<Board> paging = this.boardServiceImpl.getList(page);

        int totalPages = paging.getTotalPages();
        int currentBlock = page / 5; // 5개의 페이지 번호로 묶기
        int startPage = currentBlock * 5; // 현재 블록의 시작 페이지
        int endPage = Math.min(startPage + 4, totalPages - 1); // 현재 블록의 마지막 페이지 번호 (최대 페이지 수 제한)

        model.addAttribute("paging", paging);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "admin/board/list";
    }

    // 게시판 상세보기
    @GetMapping(value = "/detail/{bno}")
    public String detail(Model model, @PathVariable("bno") Long bno) {
        Board board = this.boardServiceImpl.getBoard(bno);
        model.addAttribute("board", board);
        return "admin/board/detail";
    }

    // 등록 페이지
    @GetMapping("/create")
    public String boardCreate(RegisterFormDTO registerFormDto) {
        return "admin/board/register";
    }

    // 등록 처리
    @PostMapping("/create")
    public String boardCreate(@Valid RegisterFormDTO registerFormDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/board/register";
        }
        this.boardServiceImpl.create(registerFormDto.getTitle(), registerFormDto.getContent());
        return "redirect:/board/list";
    }

    // 수정 페이지
    @GetMapping("/detail/{bno}/modify")
    public String modifyForm(@PathVariable Long bno, Model model) {
        Board board = boardServiceImpl.getBoard(bno);

        RegisterFormDTO registerFormDTO = new RegisterFormDTO();
        registerFormDTO.setTitle(board.getTitle());
        registerFormDTO.setContent(board.getContent());

        model.addAttribute("registerFormDTO", registerFormDTO);
        model.addAttribute("bno", bno);

        return "admin/board/modify";
    }

    // 수정 처리
    @PostMapping("/detail/{bno}/modify")
    public String modify(@PathVariable Long bno, @Valid @ModelAttribute RegisterFormDTO registerFormDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/board/modify";
        }
        boardServiceImpl.modify(bno, registerFormDTO.getTitle(), registerFormDTO.getContent());
        return "redirect:/board/detail/{bno}";
    }

    // 삭제 처리
    @PostMapping("/detail/{bno}/delete")
    public String delete(@PathVariable Long bno) {
        boardServiceImpl.delete(bno);
        return "redirect:/board/list";
    }

    // 파일 업로드 처리
    @PostMapping("/detail/{bno}/upload")
    public String uploadFile(@PathVariable Long bno, @RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "파일을 선택해주세요.";
        }

        // 파일 이름과 경로 설정
        String originalFilename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String savedFileName = uuid + "_" + originalFilename;

        // 파일을 지정된 경로에 저장
        File targetFile = new File(uploadDir + File.separator + savedFileName);
        file.transferTo(targetFile);

        // DB에 파일 정보 저장
        Board board = boardServiceImpl.getBoard(bno);
        Files files = new Files();
        files.setUuid(uuid);
        files.setFName(originalFilename);
        files.setFPath(targetFile.getAbsolutePath());
        files.setBno(board);

        board.getFileList().add(files);
        boardServiceImpl.save(board);  // 파일 정보 저장 후 board 객체 저장

        return "redirect:/board/detail/" + bno;
    }

}
