package com.example.secure.controller;

import com.example.secure.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello(){

        return ResponseEntity.ok("hello");
    }

}
