package com.project.lms.repository.member;

import com.project.lms.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {

//    Member findByMid(String mid);

//    Member findByEmail(String email);
}
