package com.springmvcproject.stickynotes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springmvcproject.stickynotes.service.impl.UserService;

import lombok.RequiredArgsConstructor;

@Controller
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
}
