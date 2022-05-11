package com.ftn.isa.security.auth;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ftn.isa.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

public class TokenAuthFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;
    private final UserService userService;

    public TokenAuthFilter(TokenUtils tokenHelper, UserService userService) {
        this.tokenUtils = tokenHelper;
        this.userService = userService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String authToken = tokenUtils.getToken(request);
        try {
            if (authToken != null) {
                String email = tokenUtils.getEmailFromToken(authToken);
                if (email != null) {
                    UserDetails userDetails = userService.loadUserByUsername(email);

                    if (tokenUtils.validateToken(authToken, userDetails)) {
                        TokenBasedAuth authentication = new TokenBasedAuth(userDetails);
                        authentication.setToken(authToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }

        } catch (ExpiredJwtException ignored) {
        }
        chain.doFilter(request, response);
    }

}