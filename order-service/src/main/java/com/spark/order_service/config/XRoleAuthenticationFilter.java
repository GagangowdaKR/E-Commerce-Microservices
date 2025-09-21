package com.spark.order_service.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
public class XRoleAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        //in gateway after validating JWT it adds X-Role.
        System.out.println("Request :- "+ request.getRequestURI());
        String roleHeader = request.getHeader("X-Role");
        System.out.println("roleHeader :- "+roleHeader);
        if(roleHeader != null && !roleHeader.isBlank()){
            var authorities = List.of(new SimpleGrantedAuthority(roleHeader));
            log.info("authorities :- {}", authorities);
            Authentication auth = new UsernamePasswordAuthenticationToken("gateway_user", null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
