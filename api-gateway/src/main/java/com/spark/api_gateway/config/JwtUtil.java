package com.spark.api_gateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private static final String SECRET = "supersecurekeywithminimum32characterslength";

    public Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public Claims extractClaims(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token){
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token){
        log.info("Extracted Role : {} ", extractClaims(token).get("role", String.class));
        return extractClaims(token).get("role", String.class);
    }

    public boolean isTokenExpired(String token){
        log.info("Token Expiration Status : {} ", extractClaims(token).getExpiration().before(new Date()));
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token){
        try{
            log.info("Validation Status : {} ", !isTokenExpired(token));
            return !isTokenExpired(token);
        }catch (Exception e){
            return false;
        }
    }
}
