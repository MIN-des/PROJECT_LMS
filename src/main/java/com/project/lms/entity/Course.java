package com.project.lms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cId;

    private String cName;
    private int credits; // 학점

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pId")
    private Professor pId;
}
