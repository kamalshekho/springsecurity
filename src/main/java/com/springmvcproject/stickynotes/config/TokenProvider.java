package com.springmvcproject.stickynotes.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// @Service
public class TokenProvider {

    @Value("${security.jwt.secret-key}")
    private String secret;

    @Value("${security.jwt.issuer}")
    private String issuer;

    // Ablaufzeit der Tokens
    @Value("${security.jwt.expiry-time-in-seconds}")
    private Long expiryTimeInSeconds;
    
    //Bekommt ein Authentication-Objekt (enthält User + Rollen),  nach erfolgreichem Login
     public String createToken(Authentication authentication) {
        String username = authentication.getName();
        List<String> authorties = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return Jwts.
                builder()
                .issuer(issuer)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (expiryTimeInSeconds * 1000)))
                .claim("username", username)
                .claim("authorities", authorties)
                // signiert mit meinem Schlüssel
                .signWith(getSecretKey()).compact();
    }

    public Authentication setAuthentication(String token) {
        Claims payload = parseClaimsFromToken(token);
        String username = payload.getSubject();
        List<String> authorities = payload.get("authorities", ArrayList.class);
        List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream().map(SimpleGrantedAuthority::new).toList();
        return new UsernamePasswordAuthenticationToken(username, "", grantedAuthorities);
    }

    public boolean validateToken(String token) {
        try {
            parseClaimsFromToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
    
    private Claims parseClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

}
