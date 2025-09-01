package com.springmvcproject.stickynotes.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class SecurityConfig {

        private final JwtAuthConverter jwtAuthConverter;

    @Bean
     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .disable())
                .authorizeHttpRequests(requests -> requests
                        .anyRequest()
                        .authenticated());
        http
                .oauth2ResourceServer(server -> server
                        .jwt()
                        .jwtAuthenticationConverter(jwtAuthConverter));

        http
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }



// without keycloak

//     private final TokenProvider tokenProvider;

// @Bean
//  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//     http
//         .csrf(csrf -> csrf.disable())
//         .sessionManagement(session -> 
//             session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//         )
//         .addFilterBefore(new JWTFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
//         .authorizeHttpRequests(auth -> auth
//             .requestMatchers("/api/users/register", "/api/users/create", "/api/users/login").permitAll()
//             .anyRequest().authenticated()
//         );

//     return http.build();
// }

//     @Bean
//     PasswordEncoder defaultPasswordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     AuthenticationManager defaultAuthenticationManager(AuthenticationConfiguration config) throws Exception {
//         return config.getAuthenticationManager();
//     }
}
