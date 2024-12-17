package com.project.lms.entity;

import com.project.lms.constant.Dept;
import com.project.lms.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@ToString
@Table(name = "orders")
public class Order extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long oId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "sId", nullable = false)
  private Student student;

  // 추가함
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cId")
  private Course course; // 강의 정보

  private Integer score; // 학점 필드 추가

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus; // 주문상태

  private LocalDateTime orderDate; // 주문일자

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
          orphanRemoval = true, fetch = FetchType.LAZY)
  private List<OrderCourse> orderCourses = new ArrayList<>();

  public void addOrderCourse(OrderCourse orderCourse) {
    orderCourses.add(orderCourse);
    orderCourse.setOrder(this);
  }
}
