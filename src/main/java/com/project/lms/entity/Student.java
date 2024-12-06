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
public class Student {

    @Id
    private String sId;
    private String sName;
    private String sPw;
    private String sTel; // 전화번호
    private String sAdd; // 주소
    private String sBirth; // 생년월일
    private String sEmail;
    private int grade; // 학년

    @Enumerated(EnumType.STRING)
    private Gen sGen; // 성별

    @Enumerated(EnumType.STRING)
    private Dept sDept;

    @Enumerated(EnumType.STRING)
    private Role role; // 독립적인 테이블이므로 필요없음
}
