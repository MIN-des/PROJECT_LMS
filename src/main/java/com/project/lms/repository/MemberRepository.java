package com.project.lms.repository;

import com.project.lms.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    List<Member> findByMid(String mid);
}
