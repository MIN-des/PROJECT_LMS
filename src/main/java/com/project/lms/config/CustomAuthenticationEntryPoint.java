package com.project.lms.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		String requestURI = request.getRequestURI();

		// 특정 경로 처리
		if (requestURI.startsWith("/student/invoices/preview")) {
			// 미리보기 요청인 경우, 401 Unauthorized 응답
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access to preview URL");
		} else if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
			// AJAX 요청인 경우, 401 Unauthorized 응답
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
		} else {
			// 그 외 요청은 로그인 페이지로 리다이렉트
			response.sendRedirect("/login");
		}
	}
}