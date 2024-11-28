package com.project.lms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "admin")
@Getter
@Setter
@ToString
//@EntityListeners(AuditingEntityListener.class)
public class Admin {

    //private Long ad_no;

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String ad_id; // 관리자 아이디

    @Column(nullable = false)
    private String ad_password; // 비밀번호

    @Column(unique = true, nullable = false)
    private String ad_rrn;  // 관리자 주민등록번호

    @Column(nullable = false)
    private String ad_name; // 관리자 이름

    @Column(unique = true, nullable = false)
    private String ad_email;

    private String ad_phone;

    private String address; // 관리자 주소

//    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
//    private Role role;

//    @OneToMany(mappedBy = "member")
//    private List<MemberRole> roles; // MemberRole과의 1:N 관계
}
