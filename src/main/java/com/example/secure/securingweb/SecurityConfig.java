package com.example.secure.securingweb;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity // 기본적인 웹 보안을 활성화 하겠다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() // http프로토콜에 대한 요청에 접근제한 설정하겠다.
                .antMatchers("/api/hello").permitAll() // 해당 요청에대해서는 인증없이 접근을 허용하겠다.
                .anyRequest().authenticated(); //  나머지 요청에 대해서는 인증을 받아야한다.
    }
}
