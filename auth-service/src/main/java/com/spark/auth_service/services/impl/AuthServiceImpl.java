package com.spark.auth_service.services.impl;

import com.spark.auth_service.config.JwtUtil;
import com.spark.auth_service.entity.User;
import com.spark.auth_service.repository.UserRepository;
import com.spark.auth_service.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<Map<String,Object>> register(User user) {
        if(userRepo.findByUsername(user.getUsername()) != null) {
            log.info("Username is already in use");
            Map<String, Object> map = Map.of("message", new UsernameNotFoundException("Username found in Database"));
            return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Password encoded is {}", passwordEncoder.encode(user.getPassword()));
        Map<String,Object> response = Map.of(
                "Message", "User Created Successfully",
                "User", userRepo.save(user)
        );
        log.info("user has been saved successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Map<String, Object>> login(String username, String password) {
        User dbUser = userRepo.findByUsername(username);
        if(dbUser == null){
            log.info("Username not found");
            Map<String, Object> response = Map.of("failure", "User not found with this Username");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if(! passwordEncoder.matches(password, dbUser.getPassword())){
            log.info("Passwords do not match : {} is Wrong", password);
            return new ResponseEntity<>(Map.of("failure", "Wrong Password"), HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> response = Map.of(
                "message", "Login Successful !!",
                "token", jwtUtil.generateToken(dbUser.getUsername(), dbUser.getRole())
        );
        log.info("jwt token has been generated and sent successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
