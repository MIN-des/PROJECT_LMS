package com.project.lms.dto;

import com.project.lms.constant.RestStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainCourseDTO {

    private Long cId;
    private String cName;
    private int credits;
    private RestStatus status;
    private int restNum;

    @QueryProjection // 반드시 있어야 Q dsl 생성됨
    public MainCourseDTO(Long cId, String cName, int credits, int restNum) {
        this.cId = cId;
        this.cName = cName;
        this.credits = credits;
        this.restNum = restNum;
    }
}
