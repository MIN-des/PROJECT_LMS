package com.project.lms.dto;

import com.project.lms.constant.Dept;
import com.project.lms.constant.OrderStatus;
import com.project.lms.entity.Course;
import com.project.lms.entity.Order;
import com.project.lms.entity.Student;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDTO {

    private Long oId;

    @NotNull(message = "강의 아이디는 필수 입력 값 입니다.")
    private Long cId; // 신청한 강의 아이디

    private StudentDTO student; // 학생 정보 DTO

    private String cName; // 강의 이름
    private int credits; // 강의 학점

    private String pId; // 교수 정보

    private Integer score; // 학점 필드 추가

    private LocalDateTime orderDate;

    private OrderStatus orderStatus;

    private Dept pDept;

    private String pName;

    private static ModelMapper modelMapper = new ModelMapper();

    public static OrderDTO of(Order order) {
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

        // 추가적으로 Course 정보를 세팅
        if (order.getCourse() != null) {
            orderDTO.setCId(order.getCourse().getCId());
            orderDTO.setCName(order.getCourse().getCName());
            orderDTO.setCredits(order.getCourse().getCredits());
            orderDTO.setPId(order.getCourse().getProfessor().getPId()); // 교수 정보 가져옴
            orderDTO.setPDept(order.getCourse().getProfessor().getPDept());
            orderDTO.setPName(order.getCourse().getProfessor().getPName());
        }

        return orderDTO;
    }
}
