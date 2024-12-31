package com.project.lms.service.admin;

import com.project.lms.dto.board.BoardDTO;

public interface BoardService {

  Long register(BoardDTO BoardDto);

  void modify(BoardDTO BoardDto);

  void remove(Long bno);
}
