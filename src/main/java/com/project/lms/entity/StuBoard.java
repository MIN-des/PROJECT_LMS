package com.project.lms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "stuBoard")
@Getter
@Setter
@ToString
public class StuBoard extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long stuBno; // 번호 정렬할 때 사용

    @Column( nullable = false)
    private String stuTitle; // 게시글 제목

    @Column(columnDefinition = "CLOB", nullable = false)
    private String stuContent; // 게시글 내용

    @ManyToOne
    @JoinColumn(name = "stuWriter", nullable = false)
    private Member stuWriter; // 게시글 작성자

    @Column(columnDefinition = "INT DEFAULT 0",nullable = false)
    private int stuViews; // 게시글 조회수

//    private List<StuBoardFile> fileList; // 게시글 첨부파일 리스트
}
