package com.example.secure.config.jwt;

import com.example.secure.config.auth.PrincipalDetails;
import com.example.secure.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * 스프링 시큐리티에 UsernamePasswordAuthenticationFilter 가 있음
 * /login 요청해서 username, password 전송하면 (POST)
 * UsernamePasswordAuthenticationFilter 동작을 함,,
 * 근데 formlogin().disable() 처리를 해놔서 동작을 안함 이거를 다시 동작시켜야
 * 그럴려면
 *  요 필터를 SecurityConfig 에서 시큐리티 필터체인에 등록!!!!
 *
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도중");
        // 1. id, pw 받아서
        try {
            // x-www-form-urlencoded 로 요청시
//            BufferedReader br = request.getReader();
//            String input = null;
//            while((input = br.readLine()) != null){
//                System.out.println(input);
//            }
//            System.out.println(request.getInputStream().toString());
            // json 으로 요청시
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);
            // 토큰 만들기
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            // PrincipalDetailsService의 loadUserByUsername() 이 실행된 후 정상이면 Authentication이 리턴됨
            // DB에 있는 username과 password가 일치한다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken); // 매니져가 인증을해서 Authentication 객체를 만들어줌

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("ㅍㅍㅍ : " + principalDetails.getUser().getUsername()); // 이게 조회가 된다는건 로그인 됫다는뜻
            System.out.println("---------------------------------");
            // authentication 객체가 Security session 영역에 저장을 해야하고 그방법이 return
            return authentication;
        } catch (IOException e) {
            e.printStackTrace(); // 에러낫을때 떠넘겨 버리면 밑에 코드가 unreacheable 되서 컴파일 에러
        }
        // 2. 정상인지 로그인 시도를 authenticationManager로 하면 PrincipalDetailsService loadUserByUsername() 가 실행됨

        // 3. PrincipalDetails 를 세션에 담고 => 세션에 값이 있어야 권한 관리가 된다. (권한관리 안할거면 세션에 안담아도 됨)

        // 4. JWT 토큰을 만들어서 응답해주면
        System.out.println("================================");
        return null;
    }

    // attemptAutentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행됨
    // JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 됨.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨 : 인증이 완료되었씁ㄴ다.");
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
