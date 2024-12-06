//package com.project.lms.service;
//
//import com.project.lms.constant.Dept;
//import com.project.lms.constant.Gen;
//import com.project.lms.constant.Role;
//import com.project.lms.dto.ProfessorDTO;
//import com.project.lms.dto.ProfessorUpdateDTO;
//import com.project.lms.entity.Professor;
//import com.project.lms.repository.ProfessorRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.annotation.Commit;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityNotFoundException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Commit // 커밋 어노테이션 없으면 DB 저장이 안 됨
//@SpringBootTest
//@Transactional
//public class ProfessorServiceTests {
//
//    @Autowired
//    private ProfessorService professorService;
//
//    @Autowired
//    private ProfessorRepository professorRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public Professor createProfessor() {
//        ProfessorDTO professorDTO = new ProfessorDTO();
//        professorDTO.setPId("P993001");
//        professorDTO.setPName("홍길동");
//        professorDTO.setPPw("1234");
//        professorDTO.setPTel("010-1234-5678");
//        professorDTO.setPAdd("Seoul, Korea");
//        professorDTO.setPBirth("1980-01-01");
//        professorDTO.setPEmail("P993001@example.com");
//        professorDTO.setYear("1999-03-04");
//        professorDTO.setPGen(Gen.MALE);
//        professorDTO.setPDept(Dept.TECH);
//        professorDTO.setRole(Role.PROFESSOR);
//
//        return Professor.createProfessor(professorDTO, passwordEncoder);
//    }
//
//    @Test
//    @DisplayName("관리자가 교수 계정 생성하는 테스트")
//    public void saveProfessorTest() {
//        Professor professor = createProfessor();
//        Professor savedProfessor = professorService.saveProfessor(professor);
//
//        assertEquals(professor.getPId(), savedProfessor.getPId());
//        assertEquals(professor.getPName(), savedProfessor.getPName());
//        assertEquals(professor.getPPw(), savedProfessor.getPPw());
//        assertEquals(professor.getPTel(), savedProfessor.getPTel());
//        assertEquals(professor.getPAdd(), savedProfessor.getPAdd());
//        assertEquals(professor.getPBirth(), savedProfessor.getPBirth());
//        assertEquals(professor.getPEmail(), savedProfessor.getPEmail());
//        assertEquals(professor.getYear(), savedProfessor.getYear());
//        assertEquals(professor.getPGen(), savedProfessor.getPGen());
//        assertEquals(professor.getPDept(), savedProfessor.getPDept());
//        assertEquals(professor.getRole(), savedProfessor.getRole());
//    }
//
//    @Test
//    @DisplayName("교수 계정 중복 생성 테스트")
//    public void saveDuplicateProfessorTest() {
//        Professor professor1 = createProfessor();
//        Professor professor2 = createProfessor();
//
//        professorService.saveProfessor(professor1);
//
//        Throwable e = assertThrows(IllegalStateException.class, () -> {
//            professorService.saveProfessor(professor2);
//        });
//
//        assertEquals("이미 등록된 계정입니다.", e.getMessage());
//    }
//
//    @Test
//    @DisplayName("관리자 교수 조회 테스트")
//    public void getProfessorTest() {
//        Professor professor = new Professor();
//        professor.setPId("professor01");
//        professor.setPName("테스트 교수");
//        professor.setPEmail("test@example.com");
//        professor.setPDept(Dept.ARTS);
//        professorRepository.save(professor);
//
//        ProfessorDTO result = professorService.getProfessor("professor01");
//
//        assertEquals("테스트 교수", result.getPName());
//        assertEquals("test@example.com", result.getPEmail());
//        assertEquals(Dept.ARTS, result.getPDept());
//    }
//
//    @Test
//    @DisplayName("비밀번호, 전화번호, 주소 업데이트 테스트")
//    public void updateProfessorTest() throws Exception {
//        // Given: 교수 데이터 저장
//        Professor professor = new Professor();
//        professor.setPId("professor02");
//        professor.setPName("테스트 교수");
//        professor.setPEmail("test@example.com");
//        professor.setPDept(Dept.ARTS);
//        professorRepository.save(professor);
//
//        // When: 업데이트 DTO 생성 및 업데이트 호출
//        ProfessorUpdateDTO updateDTO = new ProfessorUpdateDTO();
//        updateDTO.setPId("professor02");
//        updateDTO.setNewPw("12345678");
//        updateDTO.setNewTel("010-1234-5678");
//        updateDTO.setNewAdd("서울특별시");
//
//        professorService.updateMyProfessor(updateDTO);
//
//        // Then: 업데이트 결과 검증
//        Professor updatedProfessor = professorRepository.findById("professor02").orElseThrow();
//        assertEquals("010-1234-5678", updatedProfessor.getPTel());
//        assertEquals("서울특별시", updatedProfessor.getPAdd());
//    }
//
//    @Test
//    @DisplayName("교수 삭제 테스트")
//    public void deleteProfessorTest() {
//        // Given: 교수 데이터 저장
////        Professor professor = new Professor();
////        professor.setPId("professor01");
////        professor.setPName("테스트 교수");
////        professor.setPEmail("test@example.com");
////        professor.setPDept(Dept.ARTS);
////        professorRepository.save(professor);
////
////        // When: 삭제 메소드 호출
////        ProfessorDTO professorDTO = new ProfessorDTO();
////        professorDTO.setPId("professor01");
//
////        professorService.deleteProfessor(professorDTO);
//        professorRepository.findById("professor02");
//        professorRepository.deleteById("professor02");
////
////        // Then: 삭제 결과 검증
////        assertThrows(EntityNotFoundException.class, () -> {
////            professorRepository.findById("professor01").orElseThrow(EntityNotFoundException::new);
////        });
//    }
//}
