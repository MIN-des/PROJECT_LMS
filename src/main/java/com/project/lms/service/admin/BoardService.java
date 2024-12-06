package com.project.lms.service.admin;


import com.project.lms.dto.board.BoardDTO;
import com.project.lms.entity.Board;

public interface BoardService {

    Long register(BoardDTO BoardDto);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO BoardDto);

    void remove(Long bno);

    Long createBoard(Board Board);
}
