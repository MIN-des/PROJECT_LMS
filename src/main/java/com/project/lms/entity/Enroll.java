package com.project.lms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "enroll")
public class Enroll {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long eId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sId", nullable = false)
  private Student student;

  @OneToMany(mappedBy = "enroll", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<EnrollCourse> enrollCourses = new ArrayList<>(); // 장바구니 항목
}
