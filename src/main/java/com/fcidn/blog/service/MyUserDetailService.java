package com.fcidn.blog.service;

import com.fcidn.blog.config.PropertiesConfig;
import com.fcidn.blog.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    PropertiesConfig propertiesConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || !username.equals(propertiesConfig.getUserUsername())) {
            throw new ApiException("invalid credentials", HttpStatus.UNAUTHORIZED);
        }
        return User.builder()
                .username(propertiesConfig.getUserUsername())
                .password(propertiesConfig.getUserPassword())
                .build();
    }
}
