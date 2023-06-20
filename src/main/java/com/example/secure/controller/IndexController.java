package com.example.secure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //  view 를 리턴하겠다.
public class IndexController {

    @GetMapping("/")
    public String index(){
        // 머스테치 기본폴더 /src/main/resources/
        // 뷰리졸버 설정 : templates(prefix).mustache(suffix) --> application.yml에서 설정
        return "index"; // /src/main/resources/templates/index.mustache
    }

    @GetMapping("/user")
    public @ResponseBody String user(){
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/marnager")
    public @ResponseBody String manager(){
        return "manager";
    }

    // 스프링 시큐리티가 해당 요청을 낚아챔 ==> SecurityConfig 파일 생성후 작동안함.
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/join")
    public @ResponseBody String join(){
        return "join";
    }

    @GetMapping("/joinProc")
    public @ResponseBody String joinProc(){
        return "회원가입 완료됨";
    }

}
