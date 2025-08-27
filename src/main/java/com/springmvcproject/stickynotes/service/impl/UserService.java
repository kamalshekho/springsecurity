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
            // 1. Baue ein Authentication-Objekt mit Username + Password (noch nicht geprüft!)
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            // 2. authenticationManager.authenticate(...) startet den Prüfprozess:
            //    - Intern: Ruft UserDetailsService.loadUserByUsername(username) auf → DB-User laden
            //    - Der Provider: Vergleicht eingegebenes Passwort mit DB-Passwort (via PasswordEncoder)
            //    - Bei Erfolg: gibt ein vollwertiges Authentication-Objekt zurück (inkl. Rollen)

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            // Intern:  UserDetails userDetails = userDetailsService.loadUserByUsername("usernmae");
            // dann povider nimmt den passowert dem User und das von DB gehascht, dann über PasswordEncoder verglichen
            // dann wird das Authentication authentication = authenticationManager.authenticate(authenticationToken); 
            return tokenProvider.createToken(authentication);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid username or password");
        }
    }
}
