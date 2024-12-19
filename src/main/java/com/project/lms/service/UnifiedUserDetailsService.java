package com.project.lms.service;

import com.project.lms.repository.AdminRepository;
import com.project.lms.repository.ProfessorRepository;
import com.project.lms.repository.StudentRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class UnifiedUserDetailsService implements UserDetailsService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 기존에는 분리되어 있었지만 공통적인 역할을 수행하는 내용으로 변경함
        return professorRepository.findById(username)
                .map(professor -> createUserDetails(professor.getPId(), professor.getPPw(), professor.getRole().name()))
                .or(() -> studentRepository.findById(username)
                        .map(student -> createUserDetails(student.getSId(), student.getSPw(), student.getRole().name())))
                .or(() -> adminRepository.findById(username)
                        .map(admin -> createUserDetails(admin.getAId(), admin.getAPw(), admin.getRole().name())))
                .orElseThrow(() -> {
                    log.warn("Login attempt failed: username not found - {}", username);

                    return new UsernameNotFoundException("회원 정보를 찾지 못했습니다.: " + username);
                });
    }

    // UserDetails 생성 유틸 메서드
    private UserDetails createUserDetails(String id, String password, String role) {
        return new org.springframework.security.core.userdetails.User(
                id,
                password, // 암호화된 비밀번호
                List.of(new SimpleGrantedAuthority(role)) // 역할 권한 부여
        );
    }
}
