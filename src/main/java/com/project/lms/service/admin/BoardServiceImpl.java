package com.project.lms.service.admin;


import com.project.lms.dto.board.BoardDTO;
import com.project.lms.entity.Board;
import com.project.lms.repository.admin.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final ModelMapper modelMapper;

    private final BoardRepository boardRepository;

    public Board save(Board board) {
        return boardRepository.save(board);  // Board 객체 저장
    }

    public List<Board> getList() {
        return this.boardRepository.findAll();
    }

    public Board getBoard(Long bno) {
        Optional<Board> board = this.boardRepository.findById(bno);
        if (board.isPresent()) {
            return board.get();
        } else {
            log.error("board not found for ID: {}", bno);
            throw new DataNotFoundException("AllBoard not found for ID: " + bno);
        }
    }

    @Override
    public Long register(BoardDTO boardDTO) {
        Board board = modelMapper.map(boardDTO, Board.class);

        Long bno = boardRepository.save(board).getBno();

        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {
        return null;
    }

    @Override
    public void modify(BoardDTO boardDTO) {

    }

    @Override
    public void remove(Long bno) {

    }

    @Override
    public Long createBoard(Board board) {
        // Board 객체를 DTO로 변환
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setTitle(board.getTitle());
        boardDTO.setContent(board.getContent());

        // DTO를 사용하여 register 메서드 호출
        return this.register(boardDTO); // 기존의 register 메서드를 호출
    }

    public void create(String title, String content) {
        Board b = new Board();
        b.setTitle(title);
        b.setContent(content);
        b.setRegDate(LocalDate.now());
        this.boardRepository.save(b);
    }

    public void modify(Long bno, @NotEmpty(message="제목은 필수 항목입니다.") @Size(max=200) String title, @NotEmpty(message="내용은 필수 항목입니다.") String content) {
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        // 제목과 내용 수정
        board.setTitle(title);
        board.setContent(content);

        // 수정된 게시물 저장
        boardRepository.save(board);
    }
    public void delete(Long bno) {
        // 게시글 조회
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board ID: " + bno));  // 게시글이 존재하지 않으면 예외 발생

        // 게시글 삭제
        boardRepository.delete(board); // 해당 게시글을 삭제
    }

    //페이징 처리(최신순으로 조회)
    public Page<Board> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("regDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.boardRepository.findAll(pageable);
    }
}
