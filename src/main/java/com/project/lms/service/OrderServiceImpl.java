package com.project.lms.service;

import com.project.lms.constant.OrderStatus;
import com.project.lms.dto.OrderDTO;
import com.project.lms.entity.*;
import com.project.lms.repository.CourseRepository;
import com.project.lms.repository.OrderRepository;
import com.project.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final StudentRepository studentRepository;
  private final CourseRepository courseRepository;
  private final OrderRepository orderRepository;

  public void updateScore(Long oId, Integer score) {
    if (oId == null) {
      throw new IllegalArgumentException("Order ID must not be null");
    }

    // 주문 데이터 가져오기
    Order order = orderRepository.findById(oId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Order ID: " + oId));

    // 학점 업데이트
    order.setScore(score);
    orderRepository.save(order);
  }

  @Override
  public List<Order> getOrdersByCourse_cId(Long cId) {
    return orderRepository.findByCourse_cId(cId);
  }

  // 학생이 수강신청
  @Override
  public OrderDTO createOrder(OrderDTO orderDTO) {
    // 로그인한 학생 ID를 이용해 Student 엔티티 조회
    Student student = studentRepository.findById(orderDTO.getStudent().getSId())
            .orElseThrow(() -> new IllegalArgumentException("해당 학생 정보를 찾을 수 없습니다."));

    // 신청할 강의 ID를 이용해 Course 엔티티 조회
    Course course = courseRepository.findById(orderDTO.getCId())
            .orElseThrow(() -> new IllegalArgumentException("해당 강의 정보를 찾을 수 없습니다."));

    // 중복 신청 방지: 해당 학생이 이미 이 강의를 신청했는지 확인
    boolean isAlreadyOrdered = orderRepository.existsByStudentAndCourse(student, course);
    if (isAlreadyOrdered) {
      throw new IllegalArgumentException("이미 신청한 강의입니다.");
    }

    course.decreaseRestNum(); // restNum 감소
    courseRepository.save(course); // 변경 사항 저장

    // 수강신청
    Order order = new Order();
    order.setCourse(course); // 강의 정보
    order.setOrderDate(LocalDateTime.now());
    order.setOrderStatus(OrderStatus.ORDER);
    order.setStudent(student); // Order에 Student 설정

    // OrderCourse 생성 (count를 1로 고정)
    OrderCourse orderCourse = OrderCourse.createOrderCourse(course);

    // Order에 OrderCourse 추가
    order.addOrderCourse(orderCourse);

    // Order 저장
    Order savedOrder = orderRepository.save(order);

    // 결과를 OrderDTO로 변환하여 반환
    return OrderDTO.of(savedOrder);
  }

  // 학생이 신청한 내역 조회할 때 사용
  @Override
  public List<OrderDTO> getOrdersByStudent_sId(String sId) {
    List<Order> orders = orderRepository.findByStudent_sId(sId);

    if (orders.isEmpty()) { // 수강 내역이 없는 경우
      return Collections.emptyList(); // 빈 리스트 반환
    }

    // Order를 OrderDTO로 변환 (of 메서드 활용)
    return orders.stream()
            .map(OrderDTO::of)
            .collect(Collectors.toList());
  }

  // 수강 취소
  @Override
  public void cancelOrder(Long oId) {
    try {
      // 신청 내역 조회
      Order order = orderRepository.findById(oId)
              .orElseThrow(() -> new IllegalArgumentException("해당 수강 신청 내역을 찾을 수 없습니다."));

      // 관련된 강의 조회
      Course course = order.getCourse();

      if (course != null) {
        // 강의 정원 증가
        course.increaseRestNum();
        courseRepository.save(course); // 변경 사항 저장
      }

      // 신청 내역 삭제
      orderRepository.delete(order);
    } catch (Exception e) {
      e.printStackTrace(); // 예외 발생 시 오류 로그 출력
      throw e; // 예외를 다시 던져서 상위 호출자에 알림
    }
  }
}
