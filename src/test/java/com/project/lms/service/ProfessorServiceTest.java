package com.project.lms.service;

import com.project.lms.constant.Dept;
import com.project.lms.constant.Gen;
import com.project.lms.constant.Role;
import com.project.lms.dto.ProfessorDTO;
import com.project.lms.repository.ProfessorRepository;
import com.project.lms.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProfessorServiceTest {

	@Autowired
	private ProfessorService professorService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ProfessorRepository professorRepository;

	@Test
	void testCreateMultipleProfessors() {
		for (int i = 1; i <= 250; i++) {
			ProfessorDTO professorDTO = new ProfessorDTO();
			String professorId = String.format("P%07d", i); // P0000001, P0000002 ...
			professorDTO.setPId(professorId);
			professorDTO.setPName("Professor " + i);
			professorDTO.setPPw(professorId); // 비밀번호는 아이디와 동일
			professorDTO.setPTel("010-" + String.format("%04d", i) + "-" + String.format("%04d", (i * 7) % 10000));
			professorDTO.setPAdd("City " + i + ", Office Building " + (i % 20 + 1));

			// 생일: 1970-01-01 ~ 2024-12-31 사이 무작위 날짜 생성
			int year = 1970 + (i % 55);
			int month = (i % 12) + 1; // 1 ~ 12
			int day = (i % 28) + 1; // 1 ~ 28 (모든 달에 유효한 범위)
			professorDTO.setPBirth(String.format("%04d-%02d-%02d", year, month, day));

			professorDTO.setPEmail(professorId + "@test.com");

			// 년도: 1970 ~ 2024
			int joinYear = 1970 + (i % 55);
			professorDTO.setYear(String.valueOf(joinYear));

			// 성별: 짝수는 MALE, 홀수는 FEMALE
			professorDTO.setPGen(i % 2 == 0 ? Gen.MALE : Gen.FEMALE);

			// 학부: HUMAN, SOCIAL, TECH, ARTS 순환
			Dept[] depts = Dept.values();
			professorDTO.setPDept(depts[i % depts.length]);

			professorDTO.setRole(Role.PROFESSOR);

			// 교수 데이터 저장
			ProfessorDTO savedProfessor = professorService.createProfessor(professorDTO);
			System.out.println("Professor created: " + savedProfessor);
		}
	}

}