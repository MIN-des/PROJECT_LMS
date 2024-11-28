package com.project.lms.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "stuBoardFile")
@Data
public class StuBoardFile {
    @Id
    @Column(nullable = false)
    private String uuid; // 파일 아이디

    @Column(nullable = false)
    private String fileName; // 파일명

    @Column(nullable = false)
    private String filePath; // 파일 경로

    private String fileType; // 파일 유형

    @ManyToOne
    @JoinColumn(name = "stuBno", nullable = false)
    private StuBoard stuBno; // 학생 게시판 번호 fk
}
