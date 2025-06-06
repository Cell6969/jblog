package com.fcidn.blog.service;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class MyUserDetailService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO: get user and password from application.properties
        return User.builder()
                .username(username)
                .password("$2y$10$hAVo3GPBfOKqzAg/IKrw6evDUSo/y0oJuOht76YYZSWr9IbUvpfYO")
                .build();
    }
}
