package com.example.secure.controller;

import com.example.secure.model.User;
import com.example.secure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //  view 를 리턴하겠다.
public class IndexController {

    @Autowired private UserRepository userRepository;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

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
        return "loginForm";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User users){
        if(users.getUsername().contains("admin")){
            users.setRole("ROLE_ADMIN");
        } else if (users.getUsername().contains("manager")) {
            users.setRole("ROLE_MAGAGER");
        } else {
            users.setRole("ROLE_USER");
        }
        String rawPassword = users.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        users.setPassword(encPassword);
        userRepository.save(users); // 회원가입 잘됨. 비밀번호 : 1234 => 시큐리티로 로그인 할 수 없음, 이유는 패스워드가 암호화가 안되서
        return "redirect:/loginForm";
    }

    @GetMapping("/info")
    @Secured("ROLE_ADMIN")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @GetMapping("/data")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 권한 두개이상 주고싶을때 
    public @ResponseBody String data(){
        return "데이터정보";
    }

}
