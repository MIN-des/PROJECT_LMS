package com.project.lms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.type.LocalDateType;

import javax.persistence.*;
import java.time.LocalDateTime;
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

  private LocalDateTime regDate;

  private LocalDateTime modDate;

  @Column(nullable = false, columnDefinition = "INT DEFAULT 0") // 조회수 필드 매핑
  private int views;

  @OneToMany(mappedBy = "bno", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Files> fileList = new ArrayList<>();
}
