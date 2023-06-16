package com.example.secure.securingweb;

import com.example.secure.jwt.JwtAccessDeniedHandler;
import com.example.secure.jwt.JwtAuthenticationEntryPoint;
import com.example.secure.jwt.JwtSecurityConfig;
import com.example.secure.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity // 기본적인 웹 보안을 활성화 하겠다.
@EnableGlobalMethodSecurity(prePostEnabled = true) // 메소드 단위로 security 설정을 사용하기 위해
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider; // 토큰 생성, 유효성 검증
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; //  미자격 요청에 대한 401 에러리턴 (권한없음)
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler; // 필요권한 존재하지 않는경우 403 에러리 (금지)

    public SecurityConfig( // 생성자
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ){
        this.tokenProvider = tokenProvider;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(
                        "/h2-console/**" // h2 db gui 웹으로 접근하니까..
                        ,"/favicon.ico"
                ); // 해당 요청들은 모두 무시
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 토큰방식을 사용하기 때문 disable

                .exceptionHandling() // 잚못된 요청에서 오는 익셉션핸들링은 밑에 만들어놓은 클래스들을 등록해서 하겠다.
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // h2-console을 위한 설정
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션에 대한 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않으므로 stateless

                .and()
                .authorizeRequests() // http프로토콜에 대한 요청에 접근제한 설정하겠다.
                .antMatchers("/api/hello").permitAll() // 해당 요청에대해서는 인증없이 접근을 허용하겠다.
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/signup").permitAll()
                .anyRequest().authenticated() //  나머지 요청에 대해서는 인증을 받아야한다.

                .and()
                .apply(new JwtSecurityConfig(tokenProvider)); // jwt 토큰관련 설정 적용하겠다.
    }
}
