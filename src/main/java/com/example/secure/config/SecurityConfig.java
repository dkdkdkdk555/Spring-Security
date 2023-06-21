package com.example.secure.config;

import com.example.secure.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // 설정파일임
@EnableWebSecurity // 활성화 : 스프링 시큐리티 필터가 스프링 필터체인에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean //  해당 메소드의 리턴되는 오브젝트를 IoC로 등록해줌
    public BCryptPasswordEncoder encoderPwd(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 1. 구글로부터 코드받기 (인증)
     * 2. 엑세스 토큰 (권한)
     * 3. 사용자 프로필 정보를 가져와서
     * 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
     * 4-2. 그 정보가 모자라면, 추가적인 회원가입 창 나와서 진행
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // 인증이 되어야함
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // 인증 + 접근권한 있어야함
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll() // 다른요청은 권한 ok
                .and()
                .formLogin()
                .loginPage("/loginForm") // 이제 권한이 필요한 페이지는 login페이지로 가짐
                .loginProcessingUrl("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인 진행해줌 -> 컨트롤러에 (/login)을 안만들어도 됨.
                .defaultSuccessUrl("/") // 성공시 메인페이지로 이동
                .and()
                .oauth2Login()
                .loginPage("/loginForm") // 로그인폼 페이지에서 로그인 연동하겠다. 구글 로그인 url 있는곳이 해당페이지다.
                .userInfoEndpoint()
                .userService(principalOauth2UserService);// 구글 로그인이 완려된 뒤의 후처리가 필요함. TIP. 코드 안받고 엑세스 토큰 + 사용자 프로필 정보를 한방에 받음

    }
}
