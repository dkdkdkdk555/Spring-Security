package com.example.secure.securingweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity // Spring Security의 웹 보안 지원을 받으려면 필
public class WebSecurityConfig {

    @Autowired CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    /**
     * The SecurityFilterChain bean defines which URL paths should be secured and which should not.
     * Specifically, the / and /home paths are configured to not require any authentication.
     * All other paths must be authenticated.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 요청자의 의도치않은 서버 공격을 방지하는 token 검증 옵션
        http.csrf().disable(); //  단순 로그인 구현이 목적이므로 disable

        // authorizeRequests() : 경로에 권한, 인증을 설정한다는 선언
        http.authorizeHttpRequests((requests) ->
                        requests.antMatchers("/", "/home", "/api/login/fail").permitAll() // 인증이 필요없는 페이지 지정 , ("/경로/**") 이런식으로 하위경로 전체 지정도 가능
                                .anyRequest().authenticated()
                );

        http.formLogin() // Form 로그인을 선언, 요청시 Body, form-data 타입으로 전송해야함
                .loginProcessingUrl("/api/login")
                .usernameParameter("loginId").passwordParameter("password")
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler);

        http.sessionManagement().maximumSessions(1) // 최대 허용 가능 세션 수 설정 ( -1은 로그인 개수 무한대)
                .maxSessionsPreventsLogin(true); // 동시로그인 설정, true면 동시로그인 불ㅠ



        return http.build();
    }

    @Bean
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider();
    }

    /**
     * The UserDetailsService bean sets up an in-memory user store with a single user.
     */
    @Bean
    public UserDetailsService userDetailsService(){ // 인메모리 db에 싱글 유저를 저장함
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles("ADMIN", "USER")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

/**
 *  WebSecurityConfigureAdapter 상속받지 않고 Spring Security 기능 구현하는거 연구하삼.
 */


}
