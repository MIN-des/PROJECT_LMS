package com.project.lms.dto.board;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO <E> {
    //페이징 처리 DTO

    private int page;
    private int size;
    private int total;

    // start number
    private int start;

    // end number
    private int end;

    // 이전 페이지 존재 여부
    private boolean prev;

    // 다음 페이지 존재 여부
    private boolean next;

    private List<E> dtoList;

    // PageResponseDTO 생성자
    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total) {

        if (total <= 0)
            return;

        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();

        this.total = total;
        this.dtoList = dtoList;

        this.end = (int) (Math.ceil(this.page / 10.0)) * 10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil((total / (double) size)));

        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}

