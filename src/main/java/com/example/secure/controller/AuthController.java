package com.example.secure.controller;

import com.example.secure.dto.LoginDto;
import com.example.secure.dto.TokenDto;
import com.example.secure.jwt.JwtFilter;
import com.example.secure.jwt.TokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        // UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        /*
            UsernamePasswordAuthenticationToken를 이용해서 Authentication 객체를 생성하려고 .authenticatte() 메소드가 실행될때
           CustomUserDetailsService에 loadUserByUsername 메소드가 실행됨 ->  그 결과값으로 Authentication 객체가 생성됨
        * */
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 그걸 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // authentication 객체를 통해서 jwt 토큰을 생성
        String jwt = tokenProvider.createToken(authentication);

        /*
            jwt 토큰으로 response 정보를 생성해서 리턴
         */
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}
