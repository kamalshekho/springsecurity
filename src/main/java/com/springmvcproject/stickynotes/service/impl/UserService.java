package com.springmvcproject.stickynotes.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springmvcproject.stickynotes.model.entity.UserEntity;
import com.springmvcproject.stickynotes.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserEntity createUser(String username, String rawPassword, String name, String role) {
        if (userRepo.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists!");
        }

        String hashedPasswort = passwordEncoder.encode(rawPassword);

        UserEntity userEntity = UserEntity.builder().username(username).password(hashedPasswort).name(name).role(role).build();
        return userRepo.save(userEntity);
    }
}
