package com.project.lms.service;

import com.project.lms.entity.Admin;
import com.project.lms.entity.Professor;
import com.project.lms.entity.Student;
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
import java.util.Optional;

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
        // 교수 계정 확인
        Optional<Professor> professorOptional = professorRepository.findById(username);
        if (professorOptional.isPresent()) {
            Professor professor = professorOptional.get();
            return new org.springframework.security.core.userdetails.User(
                    professor.getPId(),
                    professor.getPPw(), // 암호화된 비밀번호
                    List.of(new SimpleGrantedAuthority(professor.getRole().name()))
            );
        }

        // 학생 계정 확인
        Optional<Student> studentOptional = studentRepository.findById(username);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            return new org.springframework.security.core.userdetails.User(
                    student.getSId(),
                    student.getSPw(), // 암호화된 비밀번호
                    List.of(new SimpleGrantedAuthority(student.getRole().name())) // 학생 권한 부여
            );
        }

        // 관리자 계정 확인
        Optional<Admin> adminOptional = adminRepository.findById(username);
        if (adminOptional.isPresent()) {
            log.info("Admin login detected: {}", username);
            Admin admin = adminOptional.get();
            return new org.springframework.security.core.userdetails.User(
                    admin.getAId(),
                    admin.getAPw(),
                    List.of(new SimpleGrantedAuthority(admin.getRole().name()))
            );
        }

        // 계정이 없는 경우 예외 처리
        log.warn("Login attempt failed: username not found - {}", username);
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
