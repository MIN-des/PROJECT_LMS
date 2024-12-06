package com.project.lms.repository;

import com.project.lms.constant.Role;
import com.project.lms.entity.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminRepositoryTest {
//입학처, 교무처, 총무처, 홍보처

	@Autowired
	private AdminRepository adminRepository;

	@Test
	void createAdmin() {
		Admin admin = new Admin();
		admin.setAId("A0000004");
		admin.setAPw("A0000004");
		admin.setAName("홍보처");
		admin.setRole(Role.ADMIN);

		adminRepository.save(admin);
	}
}