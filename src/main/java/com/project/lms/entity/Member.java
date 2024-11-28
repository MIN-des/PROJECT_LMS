package com.project.lms.entity;

import com.project.lms.constant.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
//@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseTimeEntity {

    @Id
    private String mid; // 멤버 아이디

    @Column(nullable = false)
    private String mname; // 멤버 이름

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<MemberRole> roles; // MemberRole과의 1:N 관계
}
