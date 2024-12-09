package com.project.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 보안 설정 커스터마이징
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable() // CSRF 보호 활성화
              .authorizeRequests()
              .antMatchers("/**").permitAll() // 테스트용으로 모든 경로 허용
              .anyRequest().authenticated();
    }

    // http 요청에 대한 보안을 설정함
    // 페이지 권한, 로그인 페이지, 로그아웃 메소드 등
    @Bean
    public PasswordEncoder passwordEncoder() { // 비밀번호 암호화하여 저장하는 함수
        return new BCryptPasswordEncoder();
    }
}
