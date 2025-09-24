package com.spark.auth_service.controller;

import com.spark.auth_service.config.JwtUtil;
import com.spark.auth_service.entity.User;
import com.spark.auth_service.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;
    public AuthController(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Validated @RequestBody User user){
        return authService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestParam String username, @RequestParam String password){
        return authService.login(username, password);
    }
}
