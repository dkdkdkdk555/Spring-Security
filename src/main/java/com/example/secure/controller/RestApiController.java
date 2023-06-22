package com.example.secure.controller;

import com.example.secure.config.auth.PrincipalDetails;
import com.example.secure.model.User;
import com.example.secure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired UserRepository userRepository;

    @GetMapping("home")
    public String home(){
        return "<h1>home</h1>";
    }

    @PostMapping("token")
    public String token(){
        return "<h1>token</h1>";
    }

    @PostMapping("join")
    public String join (@RequestBody User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return "회원가입완료";
    }

    @GetMapping("/api/v1/user") // user, manager, admin 권한만 접근 가능
    public String user(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : " + principalDetails.getUser());
        return "user";
    }
    @GetMapping("/api/v1/manager") // manager, admin 권한만 가능
    public String manager(){
        return "manager";
    }
    @GetMapping("/api/v1/admin") // admin 권한만 가능
    public String admin(){
        return "admin";
    }
}
