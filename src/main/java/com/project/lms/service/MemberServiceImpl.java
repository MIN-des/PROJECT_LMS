package com.project.lms.service;

import com.project.lms.entity.Member;
import com.project.lms.entity.MemberRole;
import com.project.lms.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional //  Member와 MemberRole 저장이 하나의 트랜잭션으로 처리되도록 함
    public Member saveMemberWithRole(String mid, List<MemberRole> roles) {
        Member member = new Member();
        member.setMid(mid);

        // Member에 권한 추가
        for (MemberRole role : roles) {
            role.setMember(member); // 관계 설정
            member.getRoles().add(role); // Member의 roles 리스트에 추가
        }

        // Member에 저장(cascade에 의해 MemberRole에도 저장됨)
        return memberRepository.save(member);
    }
}
