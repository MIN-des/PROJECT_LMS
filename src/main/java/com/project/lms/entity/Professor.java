package com.project.lms.entity;

import com.project.lms.constant.Dept;
import com.project.lms.constant.Gen;
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
public class Professor {

    @Id
    private String pId;
    private String pName;
    private String pPw;
    private String pTel; // 전화번호
    private String pAdd; // 주소
    private String pBirth; // 생년월일
    private String pEmail;
    private String year; // 입사년도

    @Enumerated(EnumType.STRING)
    private Gen pGen; // 성별

    @Enumerated(EnumType.STRING)
    private Dept pDept;

    @Enumerated(EnumType.STRING)
    private Role role;
}
