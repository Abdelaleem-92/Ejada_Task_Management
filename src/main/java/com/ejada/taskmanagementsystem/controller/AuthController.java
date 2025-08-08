package com.ejada.taskmanagementsystem.controller;

import com.ejada.taskmanagementsystem.model.User;
import com.ejada.taskmanagementsystem.security.JwtTokenProvider;
import com.ejada.taskmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));
        User user = userService.findByUsername(request.getUsername());
        String token = tokenProvider.generateToken(user);
        return ResponseEntity.ok().body(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User request) {
        return ResponseEntity.ok(userService.register(request));
    }

    public record AuthResponse(String token) {}
}
