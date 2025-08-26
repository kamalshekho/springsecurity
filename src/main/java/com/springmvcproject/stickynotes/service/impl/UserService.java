package com.springmvcproject.stickynotes.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;

import com.springmvcproject.stickynotes.config.TokenProvider;
import com.springmvcproject.stickynotes.model.entity.UserEntity;
import com.springmvcproject.stickynotes.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;



    public UserEntity createUser(String username, String rawPassword, String name, String role) {
        if (userRepo.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists!");
        }

        String hashedPasswort = passwordEncoder.encode(rawPassword);

        UserEntity userEntity = UserEntity.builder().username(username).password(hashedPasswort).name(name).role(role).build();
        return userRepo.save(userEntity);
    }

     public String loginAndGetToken(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return tokenProvider.createToken(authentication);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid username or password");
        }
    }
}
