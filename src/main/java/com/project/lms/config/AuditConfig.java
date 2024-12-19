package com.project.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditConfig {

  // 엔티티에서 @CreatedBy, @LastModifiedBy, @CreatedDate, @LastModifiedDate
  // 같은 어노테이션을 통해 데이터 생성, 수정의 메타데이터를 자동으로 기록
  @Bean
  public AuditorAware<String> auditorProvider() {
    return new AuditorAwareImpl();
  }
}
