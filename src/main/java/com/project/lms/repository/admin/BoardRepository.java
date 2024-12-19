package com.project.lms.repository.admin;


import com.project.lms.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, JpaSpecificationExecutor<Board> {
  // 모든 게시글을 페이징 처리하여 조회
  Page<Board> findAll(Pageable pageable);

  // 작성자 이름으로 게시글 조회
  Optional<Board> findByWriter(String writer);

  // 제목, 내용, 작성자에 특정 문자열이 포함된 게시글을 페이징 처리하여 조회
  Page<Board> findByTitleContainingOrContentContainingOrWriterContaining(
    String title, String content, String writer, Pageable pageable
  );

  @Query("SELECT b FROM Board b WHERE " +
    "(b.title LIKE %:keyword% OR b.content LIKE %:keyword% OR b.writer LIKE %:keyword%) " +
    "AND b.regDate BETWEEN :startDate AND :endDate")
  Page<Board> findByKeywordAndRegDateBetween(
    @Param("keyword") String keyword,
    @Param("startDate") LocalDateTime startDate,
    @Param("endDate") LocalDateTime endDate,
    Pageable pageable);

  @Query("SELECT b FROM Board b WHERE " +
    "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
    "LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
    "LOWER(b.writer) LIKE LOWER(CONCAT('%', :keyword, '%'))")
  Page<Board> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

  Page<Board> findByRegDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

  // 대소문자를 구분하지 않고 '제목'에 특정 문자열이 포함된 게시글 조회
  Page<Board> findByTitleContainingIgnoreCase(String title, Pageable pageable);

  // 대소문자를 구분하지 않고 '내용'에 특정 문자열이 포 함된 게시글 조회
  Page<Board> findByContentContainingIgnoreCase(String content, Pageable pageable);

  // 대소문자를 구분하지 않고 작성자 '이름'에 특정 문자열이 포함된 게시글 조회
  Page<Board> findByWriterContainingIgnoreCase(String writer, Pageable pageable);

  // 제목과 날짜로 검색
  Page<Board> findByTitleContainingAndRegDateBetween(
    String title, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

  // 내용과 날짜로 검색
  Page<Board> findByContentContainingAndRegDateBetween(
    String content, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

  // 작성자와 날짜로 검색
  Page<Board> findByWriterContainingAndRegDateBetween(
    String writer, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

  Page<Board> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrWriterContainingIgnoreCase(
    String title, String content, String writer, Pageable pageable);

  //수정
  Page<Board> findAllByOrderByRegDateDesc(Pageable pageable);

}