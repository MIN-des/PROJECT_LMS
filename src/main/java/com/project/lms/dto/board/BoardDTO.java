package com.project.lms.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 자동으로 생성
@NoArgsConstructor // 파라미터가 없는 디폴트 생성자를 자동으로 생성
public class BoardDTO {

    private Long bno;

    @NotEmpty
    @Size(min = 3, max = 100)
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String writer;

    private int views;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    // private List<AllBoardFile> fileList = new ArrayList<>();
}

