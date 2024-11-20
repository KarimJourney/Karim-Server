package com.karim.karim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 경로에 대해 CORS 설정
                        .allowedOrigins("http://localhost:5000") // 프론트엔드 출처
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 허용할 HTTP 메소드
                        .allowedHeaders("*") // 허용할 헤더
                        .allowCredentials(true) // 자격 증명(쿠키, 인증 헤더 등) 허용
                        .maxAge(3600); // 설정을 캐싱할 시간 (초 단위)
            }
        };
    }
}