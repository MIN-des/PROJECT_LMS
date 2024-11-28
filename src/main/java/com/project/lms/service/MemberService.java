package com.project.lms.service;

import com.project.lms.entity.Member;
import com.project.lms.entity.MemberRole;

import java.util.List;

public interface MemberService {

    public Member saveMemberWithRole(String mid, List<MemberRole> roles);
}
