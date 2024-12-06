package com.project.lms.entity;

import com.project.lms.constant.RestStatus;
import com.project.lms.dto.CourseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString
public class Course extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cId;

    @NotBlank(message = "강의명은 필수 항목입니다.")
    private String cName;

    @NotNull(message = "학점은 필수 항목입니다.")
    @Min(value = 1, message = "학점은 1 이상이어야 합니다.")
    @Max(value = 6, message = "학점은 6 이하이어야 합니다.")
    private int credits; // 학점

    @Enumerated(EnumType.STRING)
    private RestStatus status; // 잔여인원 상태

    private int restNum; // 잔여인원 숫자

    // 강의 생성 후 업데이트 하는 경우
    public void updateCourse(CourseDTO courseDTO) {
        this.cName = courseDTO.getCName();
        this.credits = courseDTO.getCredits();
        this.status = courseDTO.getStatus();
        this.restNum = courseDTO.getRestNum();
    }
}
