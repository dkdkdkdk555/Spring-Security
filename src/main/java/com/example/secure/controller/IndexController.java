package com.example.secure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //  view 를 리턴하겠다.
public class IndexController {

    @GetMapping("/")
    public String index(){
        // 머스테치 기본폴더 /src/main/resources/
        // 뷰리졸버 설정 : templates(prefix).mustache(suffix) --> application.yml에서 설정
        return "index"; // /src/main/resources/templates/index.mustache
    }
    

}
