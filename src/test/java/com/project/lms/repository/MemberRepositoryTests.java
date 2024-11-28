package com.project.lms.repository;

import com.project.lms.constant.Role;
import com.project.lms.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
// Member와 MemberRole 저장이 하나의 트랜잭션으로 처리되도록함
// 오류 발생 시 전체 트랜잭션이 롤백
@Transactional
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    public void createMembers() {
        for (int i = 1; i <= 10; i++) {
            Member member = new Member();
            member.setMid("prof" + i);
            member.setMname("professor" + i);
            member.setPassword("password" + i);
            member.setEmail("prof" + i + "@university.edu");
            member.setPhone("010-1234-567" + (i - 1));
            member.setRole(Role.PROFESSOR);

            Member savedMember = memberRepository.save(member);
        }
    }

    @Commit
    @Test
    public void findByMemberIdTest() {
        this.createMembers();

        List<Member> memberList = memberRepository.findByMid("prof3");

        for (Member member : memberList) {
            System.out.println(member);
        }
    }
}
