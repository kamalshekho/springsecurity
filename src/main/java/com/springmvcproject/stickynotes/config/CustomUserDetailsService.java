package com.springmvcproject.stickynotes.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springmvcproject.stickynotes.model.entity.UserEntity;
import com.springmvcproject.stickynotes.repository.UserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username {}", username);
        Optional<UserEntity> user = this.userRepo.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        log.info("user {}", user.get());
        String passowrd = user.get().getPassword();
        log.info("passowrd {}", passowrd);
        String role = user.get().getRole();
        log.info("role {}", role);

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(role));
        log.info("roles {}", roles);


        return new CustomUserDetails(username, role, roles);
    }
}
