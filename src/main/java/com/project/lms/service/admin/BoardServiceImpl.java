package com.project.lms.service.admin;


import com.project.lms.dto.board.BoardDTO;
import com.project.lms.entity.Admin;
import com.project.lms.entity.Board;
import com.project.lms.repository.AdminRepository;
import com.project.lms.repository.admin.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;
    private final AdminRepository adminRepository; // AdminRepository 주입

    // 게시글 저장
    public Board save(Board board) {
        return boardRepository.save(board);  // Board 객체 저장
    }

    // 게시글 목록 조회
    public List<Board> getList() {
        return this.boardRepository.findAll();
    }

    // 게시글 상세 조회
    public Board getBoard(Long bno) {
        Optional<Board> board = this.boardRepository.findById(bno);
        if (board.isPresent()) {
            return board.get();
        } else {
            log.error("board not found for ID: {}", bno);
            throw new DataNotFoundException("AllBoard not found for ID: " + bno);
        }
    }

    // 게시글 등록
    @Override
    public Long register(BoardDTO boardDTO) {
        Board board = modelMapper.map(boardDTO, Board.class);

        // Spring Security로 로그인된 사용자 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName(); // 로그인된 사용자 ID

        // AdminRepository를 사용해 aName 가져오기
        Optional<Admin> adminOpt = adminRepository.findById(userId);
        if (adminOpt.isPresent()) {
            board.setWriter(adminOpt.get().getAName()); // 작성자 설정
        } else {
            board.setWriter("Cannot resolve method 'create' in 'BoardServiceImpl'Unknown"); // 기본값 설정
        }

        Long bno = boardRepository.save(board).getBno();
        return bno;// 저장 후 게시글 번호 반환
    }

    // 게시글 생성
    public Board create(String title, String content) {
        Board board = new Board();
        board.setTitle(title);
        board.setContent(content);
        board.setRegDate(LocalDateTime.now());

        // Spring Security로 로그인된 사용자 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication != null ? authentication.getName() : "Unknown"; // 로그인된 사용자 ID

        // AdminRepository를 사용해 aName 가져오기
        String writerName = adminRepository.findById(userId)
                .map(Admin::getAName)
                .orElse("Unknown");

        board.setWriter(writerName); // 작성자 설정

        return boardRepository.save(board); // 저장 후 반환
    }

    // 게시글 수정
    @Override
    public void modify(BoardDTO boardDTO) {
    }

    // 게시글 삭제
    @Override
    public void remove(Long bno) {
    }

    // 게시글 수정 처리
    public void modify(Long bno, @NotEmpty(message = "제목은 필수 항목입니다.") @Size(max = 200) String title, @NotEmpty(message = "내용은 필수 항목입니다.") String content) {
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        // 제목과 내용 수정
        board.setTitle(title);
        board.setContent(content);

        // 수정된 게시물 저장
        boardRepository.save(board);
    }

    // 게시글 삭제 처리
    public void delete(Long bno) {
        // 게시글 조회
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board ID: " + bno));  // 게시글이 존재하지 않으면 예외 발생

        // 게시글 삭제
        boardRepository.delete(board); // 해당 게시글을 삭제
    }

    public Long createBoard(Board board) {
        // Board 객체를 DTO로 변환
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setTitle(board.getTitle());
        boardDTO.setContent(board.getContent());

        // DTO를 사용하여 register 메서드 호출
        return this.register(boardDTO); // 기존의 register 메서드를 호출
    }

    // 게시글 목록 조회 (페이징, 최신순 정렬)
    public Page<Board> getList(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("regDate"))); // 최신순 정렬
        return this.boardRepository.findAll(pageable);
    }

    // 특정 작성자로 게시글 조회
    public Board getWriter(String writer) {
        Optional<Board> board = this.boardRepository.findByWriter(writer);
        if (board.isPresent()) {
            return board.get();
        } else {
            throw new DataNotFoundException("writer not found");
        }
    }

    // 검색 (제목, 내용, 작성자로 검색 가능, 페이징 포함)
    public Page<Board> searchBoards(int page, String keyword) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("regDate")));

        if (keyword == null || keyword.trim().isEmpty()) {
            return this.boardRepository.findAll(pageable); // 검색 키워드 없을 때 빈 목록 반환
        }

        return boardRepository.findByTitleContainingOrContentContainingOrWriterContaining(
                keyword, keyword, keyword, pageable
        );

    }

    // JPQL 기반 검색
    public Page<Board> getListWithKeyword(int page, String keyword) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("regDate")));
        if (keyword == null || keyword.trim().isEmpty()) {
            return this.boardRepository.findAll(pageable); // 검색어가 없으면 전체 목록 반환
        } else {
            return this.boardRepository.findByKeyword(keyword, pageable); // 검색어로 필터링된 목록 반환
        }
    }

    // 동적 검색 (제목, 내용, 작성자 검색, 페이징 포함)
    public Page<Board> searchAll(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("regDate")));
        return boardRepository.findAll((root, query, cb) -> {
            query.distinct(true); // 중복 제거
            return cb.or(
                    cb.like(root.get("title"), "%" + keyword + "%"), // 제목
                    cb.like(root.get("content"), "%" + keyword + "%"), // 내용
                    cb.like(root.get("writer"), "%" + keyword + "%") // 작성자
            );
        }, pageable);
    }

    // 제목으로 검색
    public Page<Board> searchByTitle(String keyword, Pageable pageable) {
        return boardRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    // 내용으로 검색
    public Page<Board> searchByContent(String keyword, Pageable pageable) {
        return boardRepository.findByContentContainingIgnoreCase(keyword, pageable);
    }

    // 작성자로 검색
    public Page<Board> searchByWriter(String keyword, Pageable pageable) {
        return boardRepository.findByWriterContainingIgnoreCase(keyword, pageable);
    }

    /**
     * 날짜와 키워드를 기준으로 게시글 필터링
     *
     * @param startDate  검색 시작 날짜
     * @param endDate    검색 종료 날짜
     * @param searchType 검색 타입 (제목, 내용, 작성자)
     * @param keyword    검색 키워드
     * @param page       페이지 번호
     * @return 필터링된 게시글 페이지
     */
    public Page<Board> filterByDateAndKeyword(LocalDate startDate, LocalDate endDate, String searchType, String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : LocalDateTime.MIN;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : LocalDateTime.MAX;

        if (keyword == null || keyword.trim().isEmpty()) {
            return boardRepository.findByRegDateBetween(startDateTime, endDateTime, pageable);
        }

        switch (searchType != null ? searchType.toLowerCase() : "all") {
            case "title":
                return boardRepository.findByTitleContainingAndRegDateBetween(keyword, startDateTime, endDateTime, pageable);
            case "content":
                return boardRepository.findByContentContainingAndRegDateBetween(keyword, startDateTime, endDateTime, pageable);
            case "writer":
                return boardRepository.findByWriterContainingAndRegDateBetween(keyword, startDateTime, endDateTime, pageable);
            case "all":
            default:
                return boardRepository.findByKeywordAndRegDateBetween(keyword, startDateTime, endDateTime, pageable);
        }
    }

}