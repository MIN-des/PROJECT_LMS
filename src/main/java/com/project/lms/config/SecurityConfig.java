package com.project.lms.config;

import com.project.lms.constant.Role;
import com.project.lms.service.UnifiedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UnifiedUserDetailsService unifiedUserDetailsService;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(unifiedUserDetailsService)
      .passwordEncoder(passwordEncoder());
  }

  // 보안 설정 커스터마이징
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.headers()
      .frameOptions()
      .sameOrigin(); // SAMEORIGIN 설정으로 변경 / pdf 파일 미리보기 설정

    http.headers()
      .cacheControl()
      .disable();

    http.csrf().disable();

    http.formLogin()
      .loginPage("/login")
      .defaultSuccessUrl("/main", true)
      .usernameParameter("username")
      .failureUrl("/login?error=true")
      .and()
      .logout()
      .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
      .logoutSuccessUrl("/");

    http.authorizeRequests()
      .antMatchers("/", "/login", "/board", "/board/**", "schedule/**", "/uploaded-images/**",
        "/student/invoices/preview/**", "/invoices/download/**", "/meals/**").permitAll() // 모든 사람이 볼 수 있음, 미리보기 추가함 url 설정도 추가함
      .antMatchers("/dashboard", "/dashboard/**").permitAll()
      // 공지사항 및 학사일정 API는 누구나 접근 가능
      .antMatchers("/api/notices", "/api/schedules").permitAll()

      // 정적 자원에 대한 접근 허용(주로 쓰는 건 assets 안에 있음)
      .antMatchers("/assets/**").permitAll()

      // 시큐리티에서 정적 자원은 static 폴더 아래 위치함
      // 근데 우리가 사용하는 건 static 아래 또 다른 폴더 아래에 있음
      // 그래서 폴더 지정을 구체적으로 해줘야 됨
      .antMatchers("/charts/**", "/components/**", "/forms/**", "/maps/**", "/tables/**").permitAll()
      .antMatchers("/admin/**").hasAuthority(Role.ROLE_ADMIN.name()) // "ROLE_ADMIN"
      .antMatchers("/student/**").hasAuthority(Role.ROLE_STUDENT.name()) // "ROLE_STUDENT"
      .antMatchers("/professor/**").hasAuthority(Role.ROLE_PROFESSOR.name()) // "ROLE_PROFESSOR"
      // 채팅 접근 허용: 학생과 교수 권한
      .antMatchers("/message/**").hasAnyAuthority(Role.ROLE_STUDENT.name(), Role.ROLE_PROFESSOR.name())
      // 나머지 요청은 인증된 사용자만 접근 가능
      .anyRequest().authenticated();

    http.exceptionHandling()
      .authenticationEntryPoint((request, response, authException) -> {
        // 인증되지 않은 사용자는 로그인 페이지로 이동
        response.sendRedirect("/login");
      })
      .accessDeniedHandler((request, response, accessDeniedException) -> {
        // 권한이 없는 사용자는 로그인 페이지로 이동
        response.sendRedirect("/login");
      });

    http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint()); // 인증 문제 예외처리
  }

  // http 요청에 대한 보안을 설정함
  // 페이지 권한, 로그인 페이지, 로그아웃 메소드 등
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
