package com.example.secure.service;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    @Secured("ADMIN")
    public String secure(){
        return "관리자 계정입니다.";
    } // 성능에 어떻게 영향을 끼칠지 몰겠고 사용하면 좋은 사례도 생각이 안나서 나중에 관리자 페이지 만들면 그때 써도 좋을듯..
}
