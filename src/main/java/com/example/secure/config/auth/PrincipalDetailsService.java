package com.example.secure.config.auth;

import com.example.secure.model.User;
import com.example.secure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *  시큐리티 설정에서 .loginProcessiongUrl("/login") 해둬서
 *  /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 함수가 실행된다.
 */
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired private UserRepository userRepository;

    // Security Session <= Authentication <= UserDetails <= User
    @Override
    /*
        일반로그인시 얘도 오버라이딩 안하더라도 알아서 내부적으로 실행되서 UserDetails -> Authentication => Security Session에 저장
        그럼에도 오버라이딩 하는 이유
        1. 유저인지 검증하기 위해
        2. PrincipalDetails로 포장해 리턴해서 나중에 Authentication 꺼내 볼때 구글 로그인 경우와 나눠서 분기처리 할 필요 없어짐 개꿀
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username);
        if(userEntity != null){
            return new PrincipalDetails(userEntity); // 리턴될때 Authentication 에 들어가고 그걸 알아서 시큐리티 세션에 넣어줌 -->  로그인 완료
            // Aurhentication으로 SecuritySession에 저장되면서 @AuthenticationPrincipal 어노 활성화
        }
        return null;
    }
}
