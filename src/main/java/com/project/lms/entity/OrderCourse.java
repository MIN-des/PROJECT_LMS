package com.project.lms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class OrderCourse extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long ocId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cId") // item_id = course_id
  private Course course;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "oId") // order_id
  private Order order;

  public static OrderCourse createOrderCourse(Course course) {
    OrderCourse orderCourse = new OrderCourse();
    orderCourse.setCourse(course);

    return orderCourse;
  }
}
