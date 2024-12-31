package com.project.lms.entity;

import com.project.lms.constant.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
public class Admin {

  @Id
  private String aId; // role 1자리(A, P, S) +  입학연도 2자리 + dept 1자리 + 숫자 3자리(001...)
  private String aName; // 부서 이름만 교무처 홍보처 총무처 대외협력처
  private String aPw;

  @Enumerated(EnumType.STRING)
  private Role role;
}
