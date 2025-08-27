package com.springmvcproject.stickynotes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springmvcproject.stickynotes.service.impl.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestParam String username, @RequestParam String password,
    @RequestParam String name,@RequestParam String role) {
        try {
            userService.createUser(username, password, name, role);
            return ResponseEntity.ok("User successfully created");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @PostMapping("/login")
        public ResponseEntity<IdToken> authenticate(@RequestParam String username, @RequestParam String password) {
            String token = userService.loginAndGetToken(username, password);
            return new ResponseEntity<>(new IdToken(token), HttpStatus.OK);
        }
}

class IdToken {
    private String id_token;

    IdToken(String idToken) {
        id_token = idToken;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }
}

