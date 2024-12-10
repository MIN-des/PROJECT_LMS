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
                .mvcMatchers("/", "/login").permitAll() // 모든 사람이 볼 수 있음
                .antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico").permitAll() // 정적 자원에 대한 접근 허용
                .mvcMatchers("/admin/**").hasAuthority(Role.ROLE_ADMIN.name()) // "ROLE_ADMIN"
                .mvcMatchers("/student/**").hasAuthority(Role.ROLE_STUDENT.name()) // "ROLE_STUDENT"
                .mvcMatchers("/professor/**").hasAuthority(Role.ROLE_PROFESSOR.name()) // "ROLE_PROFESSOR"
                .anyRequest().authenticated();

        http.exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendRedirect("/access-denied");
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
