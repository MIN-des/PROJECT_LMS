package com.project.lms.entity;

import com.project.lms.constant.Dept;
import com.project.lms.constant.Gen;
import com.project.lms.constant.Role;
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
public class Professor extends BaseEntity {

  @Id
  private String pId;
  private String pName;
  private String pPw;
  private String pTel; // 전화번호
  private String pAdd; // 주소
  private String pBirth; // 생년월일
  private String pEmail;
  private int year; // 입사년도

  @Enumerated(EnumType.STRING)
  private Gen pGen; // 성별

  @Enumerated(EnumType.STRING)
  private Dept pDept;

  @Enumerated(EnumType.STRING)
  private Role role = Role.ROLE_PROFESSOR;

  @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Course> courses = new ArrayList<>();
}
