package com.project.lms.service;

import com.project.lms.dto.OrderDTO;
import com.project.lms.entity.Course;
import com.project.lms.entity.Order;
import com.project.lms.entity.Student;
import com.project.lms.repository.CourseRepository;
import com.project.lms.repository.OrderRepository;
import com.project.lms.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private OrderRepository orderRepository;

    /*@Test
    void createOrders() {
        // 이미 DB에 존재하는 studentId와 courseId 사용
        String studentId = "S2401001"; // 실제 데이터베이스에 존재하는 학생 ID
        Long courseId = 146L; // 실제 데이터베이스에 존재하는 강의 ID
        int orderCount = 1;

        // 주문 데이터를 생성
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCId(courseId);
        orderDTO.setCount(orderCount);

        // 주문 생성 호출
        Long orderId = orderService.order(orderDTO, studentId);

        // 주문 확인
        Optional<Order> createdOrder = orderRepository.findById(orderId);
    }*/
}
