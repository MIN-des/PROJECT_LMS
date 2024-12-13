package com.project.lms.dto;

import com.project.lms.constant.RestStatus;
import com.project.lms.entity.Course;
import com.project.lms.entity.Professor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class CourseDTO {

    private Long cId;

    @NotBlank(message = "강의명은 필수 항목입니다.")
    private String cName;

    @NotNull(message = "학점은 필수 항목입니다.")
    @Min(value = 1, message = "학점은 1 이상이어야 합니다.")
    @Max(value = 6, message = "학점은 6 이하이어야 합니다.")
    private int credits; // 학점

    private RestStatus status; // 잔여인원, 수강정원

    private int restNum; // 잔여인원 숫자

    private int maxCapacity; // 최대 정원 수

    private String pId; // 교수 ID

    private List<String> studentIds; // 강의를 신청한 학생 ID 목록

    // 사용할지 안할지 모름
    /*public String getStatus() {
        return restNum > 0 ? "AVAILABLE" : "FULL";
    }*/

    private static ModelMapper modelMapper = new ModelMapper();

    // Course 객체를 입력받아 CourseDTO 객체로 변환하는 메소드
    // 객체 변환을 위해 ModelMapper 사용함
    // Entity -> DTO
    public static CourseDTO of(Course course) {
        return modelMapper.map(course, CourseDTO.class);
    }

    // DTO -> Entity
    public Course toEntity() {
        return modelMapper.map(this, Course.class);
    }
}
