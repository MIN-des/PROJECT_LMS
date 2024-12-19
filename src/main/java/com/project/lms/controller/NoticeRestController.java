package com.project.lms.controller;

import com.project.lms.dto.board.BoardDTO;
import com.project.lms.service.admin.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeRestController {

  private final BoardServiceImpl boardService;

  @GetMapping("/api/notices")
  public List<BoardDTO> getRecentNotices(@RequestParam(defaultValue = "20") int limit) {
    return boardService.getRecentBoards(limit);
  }
}
