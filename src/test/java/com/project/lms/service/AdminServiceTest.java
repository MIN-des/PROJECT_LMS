package com.project.lms.service;

import com.project.lms.constant.Dept;
import com.project.lms.constant.Gen;
import com.project.lms.dto.StudentDTO;
import com.project.lms.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
class AdminServiceTest {

	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private AdminService adminService;

	@Test
	void testCreateMultipleStudents() {
		Random random = new Random();
		for (int i = 1; i <= 2; i++) {
			StudentDTO studentDTO = new StudentDTO();
			String studentId = String.format("S%07d", i); // P0000001, P0000002 ...
			studentDTO.setSId(studentId);
			studentDTO.setSName("Student " + i);
			studentDTO.setSPw(studentId); // 비밀번호는 아이디와 동일
			studentDTO.setSTel("010-" + String.format("%04d", i) + "-" + String.format("%04d", (i * 7) % 10000));
			studentDTO.setSAdd("City " + i + ", Office Building " + (i % 20 + 1));

			// 생일: 1970-01-01 ~ 2024-12-31 사이 무작위 날짜 생성
			int year = 1970 + (i % 55);
			int month = (i % 12) + 1; // 1 ~ 12
			int day = (i % 28) + 1; // 1 ~ 28 (모든 달에 유효한 범위)
			studentDTO.setSBirth(String.format("%04d-%02d-%02d", year, month, day));

			studentDTO.setSEmail(studentId + "@test.com");

			// 1 ~ 4 사이의 랜덤 숫자로 grade 설정
			int grade = random.nextInt(4) + 1; // 0 ~ 3 -> 1 ~ 4
			studentDTO.setGrade(grade);

			// 성별: 짝수는 MALE, 홀수는 FEMALE
			studentDTO.setSGen(i % 2 == 0 ? Gen.MALE : Gen.FEMALE);

			// 학부: HUMAN, SOCIAL, TECH, ARTS 순환
			Dept[] depts = Dept.values();
			studentDTO.setSDept(depts[i % depts.length]);

//			professorDTO.setRole(Role.PROFESSOR);

			// 학생 데이터 저장
			StudentDTO savedStudent = adminService.createStudent(studentDTO);
			System.out.println("Student created: " + savedStudent);
		}
	}
}