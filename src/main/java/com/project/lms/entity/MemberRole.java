package com.project.lms.entity;

import com.project.lms.constant.Role;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "memberRole")
@Getter
@Setter
@ToString
//@EntityListeners(AuditingEntityListener.class)
public class MemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long mno; // pk

    @ManyToOne
    @JoinColumn(unique = true, name = "mid", nullable = false)
    private Member member; // 학번 or 교수 아이디

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; // 권한
}

