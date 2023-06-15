package com.example.secure.securingweb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Spring Security의 웹 보안 지원을 받으려면 필
public class WebSecurityConfig {



    /**
     * The SecurityFilterChain bean defines which URL paths should be secured and which should not.
     * Specifically, the / and /home paths are configured to not require any authentication.
     * All other paths must be authenticated.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) ->
                requests.antMatchers("/", "/home").permitAll() // 인증이 필요없는 페이지 지정 , ("/경로/**") 이런식으로 하위경로 전체 지정도 가능
                        .anyRequest().authenticated()
        ).formLogin((form) -> form.loginPage("/login").permitAll()) //로그인, 로그아웃 페이지 지
                .logout((logout) -> logout.permitAll());

        return http.build();
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


}
