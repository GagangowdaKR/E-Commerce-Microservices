package com.spark.auth_service.services;

import com.spark.auth_service.entity.User;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public interface AuthService {

    ResponseEntity<Map<String, Object>> register(User user);

    ResponseEntity<Map<String, Object>> login(User user);
}
