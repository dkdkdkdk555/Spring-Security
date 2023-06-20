package com.example.secure.config.auth;

import com.example.secure.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 시큐리티가 /login 주소요청이 오면 낚아채서 로그인을 진행시킴
 * 로그인 진행이 완료 되면 시큐리티 session을 만들어준다. (Security ContextHolder 에 세션키값 저장)
 * Securtiy ContextHolder에 들어갈 수 있는 오브젝트는 정해져있다. => Authentication 객체
 *
 * Authentication 안에 User 정보가 있어야 됨.
 * USer 오브젝트 타입 => UserDetails 타입 객체여야 함.
 *
 * Security Session 여기에 세션정보 저장 => 저기 들어가는 객체는 Authentication => 저기 들어가는 유저정보 객체는 UserDetails(PrincipalDetails)
  */
public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    @Override // 해당 유저의 권한을 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
