package com.project.lms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
public class OrderCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ocId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cId")
    private Course cId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oId")
    private Order oId;

    private LocalDate regDate;

    private LocalDate modDate;
}
