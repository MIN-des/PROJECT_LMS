package com.project.lms;

import com.project.lms.service.admin.BoardServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LmsApplicationTests {

	@Autowired
	private BoardServiceImpl boardService;

	@Test
	void testJpa() {
		for (int z = 1; z <= 300; z++) {
			String title = String.format("테스트 데이터입니다:[%03d]", z);
			String content = "1. 신청기간: 2024년 12월 4일(수) 10:00 ~ 12월 6일(금) 16:00\n" +
					"\n" +
					"                면접필수: 면접일정은 해당 대학(부) 행정팀에 문의\n" +
					"\n" +
					"\n" +
					"\n" +
					"2. 신청대상: 본교 입학 후 한 학기 이상 재학 후 제적된 자\n" +
					"\n" +
					"   * 가, 나 이외의 해당자는 제적 후 최소 두 학기 이상 경과 후 신청가능함. \n" +
					"\n" +
					"   * 학칙에 의하여 징계(영구제적)된 자는 재입학 신청불가임. \n" +
					"\n" +
					"  가. 휴학기간 경과로 제적된 자 \n" +
					"\n" +
					"  나. 미등록으로 제적된 자\n" +
					"\n" +
					"  다. 성적불량으로 제적된 자\n" +
					"\n" +
					"  라. 자퇴자\n" +
					"\n" +
					"\n" +
					"\n" +
					"3. \uFEFF\uFEFF폐지학과 신청불가(학적관리위원회 결정사항)\n" +
					"\n" +
					"  가. 재입학 전형은 원 소속학과(부)로 지원하는 것이 원칙이나 폐지된 학과(부)로는 신청불가입니다.\n" +
					"\n" +
					"  나. 재입학 신청자의 소속학과(부)가 폐지된 경우 재입학신청서류 중 재입학 원서 나. 항목의 '소속변경 동의서'에 동의한 \n" +
					"\n" +
					"      후 변경된 학과(부)로 신청이 가능합니다.(변경된 학과(부)는 소속 대학 행정팀으로 문의)\n" +
					"\n" +
					"\n" +
					"\n" +
					"4. 제출서류\n" +
					"\n" +
					"  가. 재입학 신청서류(붙임양식) 1부. (재입학 원서, 재입학 신청사유 및 학업계획서, 재입학 서약서)\n" +
					"\n" +
					"  나. 학적 증명서 1부. (※포탈(온라인)에서 발급 가능)\n" +
					"\n" +
					"  다. 성적증명서 1부.\n" +
					"\n" +
					"\n" +
					"\n" +
					"5. 서류접수처: 해당 대학(부) 행정팀(세종캠퍼스는 학과 행정팀으로 제출)\n" +
					"\n" +
					"\n" +
					"\n" +
					"6. 면접일정: 2024년 12월 11일(수) ~ 12월 13일(금) 중 해당 학과(부)의 일정에 따름.\n" +
					"\n" +
					" \n" +
					"\n" +
					"7. 합격자 발표: 2025년 1월 17일(금) 17:00 예정\n" +
					"\n" +
					"\n" +
					"\n" +
					"8. 유의사항\n" +
					"\n" +
					"  가. 재입학은 정원의 결원을 고려하여 지원자 별로 1회에 한하여 허가합니다.\n" +
					"\n" +
					"     * 접수마감 후 정원을 채우지 못한 학과(부)의 경우 다른 학과(부)에 해당 정원이 배정되니 정원 현황에 상관 없이 \n" +
					"\n" +
					"       재입학 신청은 가능합니다.(정원 현황은 비공개)\n" +
					"\n" +
					"  나. 수강신청 및 등록금 납부는 재입학이 허가된 자에 한하고, 반드시 정규 등록기간에 등록을 완료해야 합니다. \n" +
					"\n" +
					"    미등록 시 재입학 합격은 취소되고 재지원이 불가합니다.\n" +
					"\n" +
					"  다. 재입학자는 학칙 및 재입학 서약서를 준수하여야 합니다.\n" +
					"\n" +
					"  라. 재입학 첫 학기에는 일반휴학을 할 수 없습니다.\n" +
					"\n" +
					"  마. 재입학 신청자의 소속이 폐지된 학과(부)에 해당하는 경우 재입학원서[양식]에 있는 '소속변경동의서'에 동의 후 변경된 \n" +
					"\n" +
					"      학과(부)로 지원이 가능합니다.법학과는 폐지학과에 해당되며 [법과대학 소속 학생의 재입학 절차에 관한 내규]에 따라 \n" +
					"\n" +
					"      재입학 신청이 가능합니다.(법학과 재입학 신청자는 법학전문대학원 행정팀으로 우선 문의)\n" +
					"\n";
			boardService.create(title, content);
		}
	}
}
