package com.project.lms.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

  // 현재 로그인된 사용자의 인증 정보를 가져오는 메소드
  @Override
  public Optional<String> getCurrentAuditor() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = "";

    if (authentication != null) {
      userId = authentication.getName(); // 인증된 사용자의 ID를 가져옴
    }

    // 반환값을 Optional로 감싸 JPA가 요구하는 방식에 맞게 반환함
    return Optional.of(userId);
  }
}
