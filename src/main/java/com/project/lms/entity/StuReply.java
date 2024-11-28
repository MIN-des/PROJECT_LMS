package com.project.lms.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "stuReply")
@Data
public class StuReply extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long stuRno; // 번호 정렬할 때 사용

    @ManyToOne
    @JoinColumn(name = "stuBno", nullable = false)
    private StuBoard stuBno; // 학생 게시판 번호 fk

    @Column(columnDefinition = "CLOB", nullable = false)
    private String stuReply; // 학생 게시판 댓글 내용

    @JoinColumn(name = "stuReplyer", nullable = false)
    private String stuReplyer; // 댓글 작성자
}
