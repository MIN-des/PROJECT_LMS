package com.project.lms.repository;

import com.project.lms.constant.Role;
import com.project.lms.entity.Member;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
public class MemberRepository {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void createMembers() {
        for (int i = 1; i <= 10; i++) {
            Member member = new Member();
            member.setMid("prof" + i);
            member.setMname("Professor " + i);
            member.setPassword("password" + i);
            member.setEmail("prof" + i + "@university.edu");
            member.setPhone("010-1234-567" + (i - 1));
            member.setRole(Role.PROFESSOR);

        }
    }
}
