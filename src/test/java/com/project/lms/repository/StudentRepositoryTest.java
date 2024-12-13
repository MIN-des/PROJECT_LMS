package com.project.lms.repository;

import com.project.lms.constant.Dept;
import com.project.lms.constant.Gen;
import com.project.lms.constant.Role;
import com.project.lms.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentRepositoryTest {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void createStudent() {
		Student student = new Student();
		student.setSId("S2401001");
		student.setSName("테스트");

		String rawPassword = "S2401001";
		String encodedPassword = passwordEncoder.encode(rawPassword);
		student.setSPw(encodedPassword);

		student.setSTel("010-0000-0000");
		student.setSAdd("서울시 구로구");
		student.setSBirth("2004-01-01");
		student.setSEmail("test@test.com");
		student.setGrade(1);
		student.setSGen(Gen.MALE);
		student.setSDept(Dept.HUMAN);
//		student.setRole(Role.STUDENT);

		studentRepository.save(student);
	}

	@Test
	void updateStudent() {
		String id = "S2401001";
	}

}