package com.project.lms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class EnrollCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eId")
    private Enroll eId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cId")
    private Course cId;

    private int count; // 장바구니에 담은 강의 개수
}
