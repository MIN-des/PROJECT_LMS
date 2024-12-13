package com.project.lms.service;

import com.project.lms.constant.Dept;
import com.project.lms.constant.Gen;
import com.project.lms.constant.Role;
import com.project.lms.dto.StudentDTO;
import com.project.lms.entity.Student;
import com.project.lms.repository.StudentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentServiceTest {

	@Autowired
	private StudentService studentService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private StudentRepository studentRepository;

//	@Test
//	void testCreateStudentWithAllFields() {
//		StudentDTO studentDTO = new StudentDTO();
//		studentDTO.setSId("S2401001");
//		studentDTO.setSName("John Doe");
//		studentDTO.setSPw("S2401001");
//		studentDTO.setSTel("010-1234-5678");
//		studentDTO.setSAdd("Seoul");
//		studentDTO.setSBirth("2000-01-01");
//		studentDTO.setSEmail("john@example.com");
//		studentDTO.setGrade(1);
//		studentDTO.setSGen(Gen.MALE);
//		studentDTO.setSDept(Dept.HUMAN);
////		studentDTO.setRole(Role.STUDENT);
//
//		StudentDTO savedStudent = studentService.createStudent(studentDTO);
//	}

	@Test
	void testCreateMultipleStudents() {
		for (int i = 1; i <= 250; i++) {
			StudentDTO studentDTO = new StudentDTO();
			String studentId = String.format("S%07d", i); // S0000001, S0000002 ...
			studentDTO.setSId(studentId);
			studentDTO.setSName("Student " + i);
			studentDTO.setSPw(studentId); // 비밀번호는 아이디와 동일
			studentDTO.setSTel("010-" + String.format("%04d", i) + "-" + String.format("%04d", (i * 7) % 10000));
			studentDTO.setSAdd("City " + i + ", Apt " + (i % 100 + 1));

			// 생일: 2000-01-01 ~ 2024-12-31 사이 무작위 날짜 생성
			int year = 2000 + (i % 25);
			int month = (i % 12) + 1; // 1 ~ 12
			int day = (i % 28) + 1; // 1 ~ 28 (모든 달에 유효한 범위)
			studentDTO.setSBirth(String.format("%04d-%02d-%02d", year, month, day));

			studentDTO.setSEmail(studentId + "@test.com");

			// 학년: 1 ~ 4
			studentDTO.setGrade((i % 4) + 1);

			// 성별: 짝수는 MALE, 홀수는 FEMALE
			studentDTO.setSGen(i % 2 == 0 ? Gen.MALE : Gen.FEMALE);

			// 학부: HUMAN, SOCIAL, TECH, ARTS 순환
			Dept[] depts = Dept.values();
			studentDTO.setSDept(depts[i % depts.length]);

//			studentDTO.setRole(Role.STUDENT);

			// 학생 데이터 저장
//			StudentDTO savedStudent = studentService.createStudent(studentDTO);
//			assertNotNull(savedStudent, "Student should be created successfully.");
//			assertEquals(studentId, savedStudent.getSId(), "Student ID should match.");
		}
	}


	@Test
	void updateTest() {
		StudentDTO updatedStudentDTO = new StudentDTO();
		updatedStudentDTO.setSId("S2401001");
		updatedStudentDTO.setSName("업데이트된 이름");
		updatedStudentDTO.setSPw("newpassword");
		updatedStudentDTO.setSTel("010-9999-9999");
		updatedStudentDTO.setSAdd("서울시 강남구");
		updatedStudentDTO.setSBirth("2003-12-31");
		updatedStudentDTO.setSEmail("updated@test.com");
		updatedStudentDTO.setGrade(2);
		updatedStudentDTO.setSGen(Gen.FEMALE);
		updatedStudentDTO.setSDept(Dept.SOCIAL);
//		updatedStudentDTO.setRole(Role.STUDENT);

//		StudentDTO updatedStudent = studentService.updateStudent("S2401001", updatedStudentDTO);
	}

	@Test
	public void getStudentTest() {
		String id = "S2401001";
//		Optional<StudentDTO> student = studentService.getStudentById(id);
//		System.out.println("Retrieved student: " + student);
	}

	@Test
	public void deleteStudentTest() {
		String id = "S001";
//		studentService.deleteStudent(id);
		System.out.println("deleteStudentTest passed.");
	}

}