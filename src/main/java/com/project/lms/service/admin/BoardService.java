package com.project.lms.service.admin;


import com.project.lms.dto.board.BoardDTO;
import com.project.lms.entity.Board;

public interface BoardService {

  Long register(BoardDTO BoardDto);

  void modify(BoardDTO BoardDto);

  void remove(Long bno);
}
