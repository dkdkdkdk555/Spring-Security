package com.example.secure.config;

import com.example.secure.config.jwt.JwtAuthenticationFilter;
import com.example.secure.config.jwt.JwtAuthorizationFilter;
import com.example.secure.filter.MyFilter1;
import com.example.secure.filter.MyFilter3;
import com.example.secure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 않겠다.
                .and()
                .addFilter(corsFilter) // 크로스 오리진 무시하고 모든 요청 받는다.
                .formLogin().disable() // 폼로그인 안쓴다. --> /login 이거 자동으로 동작안함
                .httpBasic().disable() // httpbasic 방식 안쓴다.
                .addFilter(new JwtAuthenticationFilter(authenticationManager())) // 로그인진행하는 필터기 때문에 AuthenticationManager 던져줘야함
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();

    }
}
