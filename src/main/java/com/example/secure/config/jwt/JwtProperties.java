package com.example.secure.config.jwt;

public interface JwtProperties {
    String SECRET = "ukha";
    int EXPIRATION_TIME = 60000*10; // 10일
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
