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
  @JoinColumn(name = "enroll_id", nullable = false)
  private Enroll enroll;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id", nullable = false)
  private Course course;

}
