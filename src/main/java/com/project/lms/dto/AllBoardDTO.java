package com.project.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 자동으로 생성
@NoArgsConstructor // 파라미터가 없는 디폴트 생성자를 자동으로 생성
public class AllBoardDTO {

    private Long allBno;

    @NotEmpty
    @Size(min = 3, max = 100)
    private String allTitle;

    @NotEmpty
    private String allContent;

    @NotEmpty
    private String allWriter;

    private int allViews;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    // private List<AllBoardFile> fileList = new ArrayList<>();
}
