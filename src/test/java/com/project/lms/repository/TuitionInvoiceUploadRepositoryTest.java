package com.project.lms.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TuitionInvoiceUploadRepositoryTest {

	@Autowired
	private TuitionInvoiceUploadRepository uploadRepository;

	@Test
	public void testExistsBytIdAndStudent_sId() {
		Long testTId = 17L; // 실제 데이터베이스에 있는 tId 사용
		String testSId = "S0000002"; // 실제 데이터베이스에 있는 sId 사용

		boolean result = uploadRepository.existsBytIdAndStudent_sId(testTId, testSId);
		System.out.println("Result: " + result);
		assertTrue(result); // 예상 결과가 true라면
	}


}