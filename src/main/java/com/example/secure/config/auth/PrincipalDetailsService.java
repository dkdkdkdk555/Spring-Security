package com.example.secure.config.auth;

import com.example.secure.model.User;
import com.example.secure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *  http://localhost:8080/login 요청 올때 동작
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetaiilsService :: loadUserByUsername()");
        User userEntity = userRepository.findByUsername(username);
        System.out.println("DB Connection :: UserRepository");
        return new PrincipalDetails(userEntity);
    }
}
