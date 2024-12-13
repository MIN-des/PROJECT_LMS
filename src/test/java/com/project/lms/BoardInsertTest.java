package com.project.lms;


import com.project.lms.entity.Board;
import com.project.lms.repository.admin.BoardRepository;
import com.project.lms.service.admin.BoardServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BoardInsertTest {

  @Autowired
  private BoardRepository boardRepository;
  private BoardServiceImpl boardService;

  @Test
  public void testInsertAllBoard() {
    Board board = new Board();
    board.setTitle("테스트 제목");
    board.setContent("테스트 내용");
    board.setWriter("테스트 작성자");

    Board savedBoard = boardRepository.save(board);

    assertNotNull(savedBoard.getBno());
    assertNotNull(savedBoard.getRegDate());
    assertNotNull(savedBoard.getModDate());
  }

  @Test
  void testJpa() {
    Board b1 = new Board();
    b1.setTitle("2025학년도 대학 신입학생 입학전형 주요사항");
    b1.setContent("2025학년도 대학 신입학생 입학전형 주요사항을 공지하오니, 아래 첨부파일을 확인하시기 바랍니다.\n" +
            "\n" +
            "본 <입학전형 주요사항>은 한국대학교육협의회 심의 및 승인 결과에 따라 추후 변경될 수 있습니다. .\n" +
            "\n" +
            "                                                    <주요 사항>\n" +
            "\n" +
            "■ 학생부종합(수시모집 기회균형선발특별전형Ⅰ) 저소득 가구 학생 지원자격 확대\n" +
            "    고등학교 졸업자(2025년 2월 졸업예정자 포함) 또는 법령에 의하여 고등학교 졸업 이상의 학력이\n" +
            "    있다고 인정된 자로서2013년 9월부터 지원서 접수 마감일까지 아래 중 하나의 자격을 유지하고 있는 자\n" +
            "    가. 국민기초생활 보장법 제2조 제1호(수급권자) 또는 제2호(수급자)의 가구 학생\n" +
            "    나. 국민기초생활 보장법 제2조 제10호에 따른 차상위계층 중 복지급여(차상위 자활급여,\n" +
            "         차상위 장애수당, 차상위 장애인연금부가급여, 차상위 본인부담경감)를 받고 있는 가구 학생\n" +
            "         또는 차상위계층 확인서 발급 대상 가구 학생\n" +
            "    다. 한부모가족지원법 제5조 및 제5조 2에 따른 지원대상 가구 학생\n" +
            "\n" +
            "■ 학생부종합(정시모집 기회균형선발특별전형Ⅱ) 전형요소별 반영 비율 명시\n" +
            "    전형방법: 서류평가와 면접(사범대학의 경우 교직적성·인성면접 포함) 점수를 합산하여 합격자를 선발함." +
            "2025학년도 정시모집 기회균형선발특별전형Ⅱ 전형요소별 반영 비율\n" +
            "모집단위\t전형요소 및 배점\n" +
            "전 모집단위\t서류평가(60) + 면접(40)\n" +
            "  * 인문, 과학, 예체능은 실기능력을 서류평가에 반영함");
    b1.setWriter("학사팀");
    b1.setRegDate(LocalDateTime.now());
    b1.setViews(0);
    this.boardRepository.save(b1);

    Board b2 = new Board();
    b2.setTitle("2025학년도 제1학기 재입학 전형 시행 안내");
    b2.setContent("1. 신청기간: 2024년 12월 4일(수) 10:00 ~ 12월 6일(금) 16:00\n" +
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
            "\n");
    b2.setWriter("학사팀");
    b2.setRegDate(LocalDateTime.now());
    b2.setViews(0);
    this.boardRepository.save(b2);
  }
  @Test
  public void testFindByWriter() {
    Optional<Board> board = boardRepository.findByWriter("testWriter");
    assertTrue(board.isPresent());
    assertEquals("testWriter", board.get().getWriter());
  }




}