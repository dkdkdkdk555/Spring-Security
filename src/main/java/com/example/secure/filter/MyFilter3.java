package com.example.secure.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("필터3");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        /*
           id, pw 들어와서 정상적으로 로그인 되면 토큰 만들어주고 그걸 응답해줌
           요청할 때 마다 header 에 Authorization 에 value 값으로 토큰을 가지고 오면
           이 토큰이 내가 만든(서버가) 토큰이 맞는지 검증만 하면 됨. ( RSA, HS256 으로)
         */
        if(req.getMethod().equals("POST")) {
            System.out.println("POST 요청됨");
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);

            if("cors".equals(headerAuth)){
                chain.doFilter(req, res);
            } else {
                PrintWriter out = res.getWriter();
                out.println("인증안됨");
            }
        } else {
            chain.doFilter(request, response); // 필터 체인에 등록
        }
    }
}
