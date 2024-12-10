package com.project.lms.repository;

import com.project.lms.constant.Role;
import com.project.lms.entity.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class AdminRepositoryTest {
//입학처, 교무처, 총무처, 홍보처

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void createAdmin() {
        Admin admin = new Admin();
        admin.setAId("A0000005");

        String password = passwordEncoder.encode("A0000005");

        admin.setAPw(password);
        admin.setAName("홍보처");
        admin.setRole(Role.ROLE_ADMIN);

        adminRepository.save(admin);
    }
}