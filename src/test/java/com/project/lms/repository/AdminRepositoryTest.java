package com.project.lms.repository;

import com.project.lms.constant.Role;
import com.project.lms.entity.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminRepositoryTest {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Test
	void createAdmin() {
		Admin admin = new Admin();
		admin.setAId("A0000001");

		// 비밀번호 암호화
		String rawPassword = "A0000001"; // 원본 비밀번호
		String encodedPassword = passwordEncoder.encode(rawPassword); // 암호화된 비밀번호
		admin.setAPw(encodedPassword);

		admin.setAName("홍보처");
		admin.setRole(Role.ROLE_ADMIN);

		adminRepository.save(admin);

		// 암호화된 비밀번호가 저장되는지 확인
		Admin savedAdmin = adminRepository.findById("A0000001").orElseThrow();
		assertTrue(passwordEncoder.matches(rawPassword, savedAdmin.getAPw())); // 암호화된 비밀번호 확인
	}
}
