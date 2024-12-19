package com.project.lms.entity;

import com.project.lms.dto.CourseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Course extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cId;

  @NotBlank(message = "강의명은 필수 항목입니다.")
  private String cName;

  @NotNull(message = "학점은 필수 항목입니다.")
  @Min(value = 1, message = "학점은 1 이상이어야 합니다.")
  @Max(value = 6, message = "학점은 6 이하이어야 합니다.")
  private int credits; // 학점

  @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Order> orders;

  private int restNum; // 잔여인원 숫자
  private int maxCapacity; // 최대 정원 수

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pId", nullable = false) // 교수 ID를 외래키로 사용
  private Professor professor;

  // 강의 생성 시 초기화 메서드
  @PrePersist
  public void initializeMaxCapacityAndRestNum() {
    if (this.maxCapacity > 0) {
      this.restNum = this.maxCapacity;
    } else {
      throw new IllegalArgumentException("수강 정원(maxCapacity)은 0보다 커야 합니다.");
    }
  }

  // 강의 생성 후 업데이트 하는 경우 나머지
  public void updateCourse(CourseDTO courseDTO) {
    this.cName = courseDTO.getCName();
    this.credits = courseDTO.getCredits();
//        this.maxCapacity=courseDTO.getMaxCapacity(); // 수정할 수 있지만 수정하면 문제 생길듯
    this.restNum = courseDTO.getRestNum();
  }

  // 남은 강의 수 감소
  public void decreaseRestNum() {
    if (this.restNum <= 0) {
      throw new IllegalArgumentException("강의 정원이 모두 찼습니다.");
    }

    this.restNum -= 1;
  }

  // 남은 강의 수 증가
  public void increaseRestNum() {
    if (this.restNum >= maxCapacity) {
      throw new IllegalArgumentException("강의 정원이 이미 가득 찼습니다.");
    }

    this.restNum += 1;
  }
}
