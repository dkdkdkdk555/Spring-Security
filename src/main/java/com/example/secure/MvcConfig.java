package com.example.secure;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 대문페이지 경로 설정
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        // hello
        registry.addViewController("/hello").setViewName("hello");
        // 로그인 페이지
        registry.addViewController("/login").setViewName("login");
    }
}
