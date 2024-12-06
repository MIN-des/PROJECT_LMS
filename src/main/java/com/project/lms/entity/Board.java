package com.project.lms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    private String writer;

    private LocalDate regDate;

    private LocalDate modDate;

    private int views;

    @OneToMany(mappedBy = "bno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Files> fileList = new ArrayList<>();
}
