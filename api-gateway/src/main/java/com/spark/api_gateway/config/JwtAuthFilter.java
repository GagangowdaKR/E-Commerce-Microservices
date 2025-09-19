package com.spark.api_gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil){
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(JwtAuthFilter.Config config) {
        return (exchange, chain) ->{
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            log.info("authHeader:{}", authHeader);

            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                log.info("authHeader:{} is Unauthorized", authHeader);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);
            log.info("token:{}", token);

            if(! jwtUtil.validateToken(token)){
               exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
               log.info("token:{} is Unauthorized", token);
               return exchange.getResponse().setComplete();
            }

            String role = jwtUtil.extractRole(token);
            log.info("Extracted role in JwtAuthFilter :{}", role);

            log.info("Exchange {}", exchange);
            exchange = exchange.mutate()
                    .request(r -> r.headers(h -> h.add("X-Role", role)))
                    .build();
            log.info("Exchange {}", exchange);
            
            return chain.filter(exchange);
        };
    }

    public static class Config {}
}
