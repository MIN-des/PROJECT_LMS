package com.project.lms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // "/uploaded-images/**" URL 패턴으로 접근 시 "C:/upload/" 디렉토리에서 파일 제공
        registry.addResourceHandler("/uploaded-images/**")
                .addResourceLocations("file:C:/upload/"); // 로컬 디렉토리 매핑
    }
}
