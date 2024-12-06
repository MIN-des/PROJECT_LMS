package com.project.lms.entity;

import com.project.lms.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sId")
    private Student sId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태

    private LocalDate orderDate; // 주문일자

    private LocalDate regDate;

    private LocalDate modDate;

    @OneToMany(mappedBy = "oId", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderCourse> orderCourses = new ArrayList<>();
}
