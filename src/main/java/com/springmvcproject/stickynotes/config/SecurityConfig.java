package com.springmvcproject.stickynotes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    SecurityFilterChain defauSecurityFilterChain(HttpSecurity http) throws Exception{

        http.authorizeHttpRequests(authRequest -> {
            authRequest.requestMatchers("/about", "css/**").permitAll();
            authRequest.requestMatchers("/", "/note/**", "/my-notes/**").authenticated();
            authRequest.requestMatchers("/users/**").hasRole("ADMIN");
            authRequest.anyRequest().permitAll();
        });
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());

        return http.build();

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
