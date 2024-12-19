package com.project.lms.controller.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.PersistenceException;


@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public String handleAccessDeniedException(AccessDeniedException ex, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("error", ex.getMessage());

    return "redirect:/courses/list";
  }

  // SQLSyntaxErrorException 예외 처리
  @ExceptionHandler(org.hibernate.exception.SQLGrammarException.class)
  public ResponseEntity<String> handleSQLGrammarException(org.hibernate.exception.SQLGrammarException ex) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("text", "plain", java.nio.charset.StandardCharsets.UTF_8)); // UTF-8 명시

    // 데이터 길이가 너무 큰 경우 처리
    if (ex.getCause().getMessage().contains("Data too long")) {
      return new ResponseEntity<>("이미지가 너무 커서 저장할 수 없습니다. 이미지 크기를 확인해주세요.", headers, HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", headers, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // 일반적인 PersistenceException 처리
  @ExceptionHandler(PersistenceException.class)
  public ResponseEntity<String> handlePersistenceException(PersistenceException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body("데이터 저장 중 오류가 발생했습니다. 입력값을 확인해주세요.");
  }
}
