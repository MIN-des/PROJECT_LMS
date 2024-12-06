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
public class Admin {

    @Id
    private String aId; // role 1자리 + dept 2자리 + 입학 2자리 + 숫자 3자리(001...)
    private String aName; // 부서 이름만
    private String aPw;

    @Enumerated(EnumType.STRING)
    private Role role;
}
