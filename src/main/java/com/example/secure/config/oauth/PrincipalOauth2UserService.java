package com.example.secure.config.oauth;

import com.example.secure.config.auth.PrincipalDetails;
import com.example.secure.model.User;
import com.example.secure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService { // 구글 로그인 후처리

    @Autowired UserRepository userRepository;

    @Override // 구글로 부터 받은 userRequest에 대한 후처리
    /*
        얘를 오버라이딩 안해도 내부적으로 알아서 userRequest를 Authentication 객체로 바꿔서 Security Session 에 저장하는데
        근데도 오버라이딩 하는 이유는
        1. 구글로 부터 받은 정보로 유저인지 검증하고 아니면 회원가입 시키기 위해
        2. 일반 로그인시와 동일하게 PrincipalDetails로 포장해 Authentication 객체 접근시 분기없이 캐스팅해서 받으려
     */
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); //  registrationId로 어떤 OAuth로그인했는지 확인가능
        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client라이브러리) -> AccessToken 요청
        // userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 받아준다.
        System.out.println("getAttributes : " + oAuth2User.getAttributes());

        // 구글 로그인 데이터 겟
        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oAuth2User.getAttribute("sub"); // google id key
        String username = provider + "_" + providerId; // 유저네임 중복 방지
        String password = new BCryptPasswordEncoder().encode(providerId);
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        // 회원가입 여부 검증
        User userEntity = userRepository.findByUsername(username);
        if(userEntity == null){
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }

        // 회원가입 진행
        return new PrincipalDetails(userEntity); // Aurhentication으로 SecuritySession에 저장되면서 @AuthenticationPrincipal 어노 활성화
    }
}
