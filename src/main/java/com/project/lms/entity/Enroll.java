package com.project.lms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Enroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eId;

    @JoinColumn(name = "cId")

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sId")
    private Student sId;
}
