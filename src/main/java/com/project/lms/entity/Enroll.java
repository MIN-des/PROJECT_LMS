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
  @JoinColumn(name = "student_id", nullable = false)
  private Student student;

  @OneToMany(mappedBy = "enroll", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<EnrollCourse> enrollCourses = new ArrayList<>(); // 장바구니 항목

  // 장바구니에 강의를 추가하는 메소드
  public void addCourse(Course course) {
    EnrollCourse enrollCourse = new EnrollCourse();
    enrollCourse.setEnroll(this);
    enrollCourse.setCourse(course);
    this.enrollCourses.add(enrollCourse);
  }

  // 장바구니에서 강의를 삭제하는 메소드
  public void removeCourse(Long courseId) {
    this.enrollCourses.removeIf(item -> item.getCourse().getCId().equals(courseId));
  }


}
