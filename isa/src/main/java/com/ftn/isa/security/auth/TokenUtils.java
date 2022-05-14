package com.ftn.isa.security.auth;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtils {

    @Value("spring-security-example")
    private String APP_NAME;

    @Value("$ECR3T-K3Y")
    public String SECRET;

    // 18000000 = 5h
    @Value("18000000")
    private int EXPIRES_IN;

    @Value("Authorization")
    private String AUTH_HEADER;

    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;


    public String generateToken(String email, String role) {
        return Jwts.builder()
                .claim("role", role)
                .setIssuer(APP_NAME)
                .setSubject(email)
                .setAudience("web")
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, SECRET).compact();
    }

    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + EXPIRES_IN);
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);

        // Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0
        //          IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
        if (authHeader != null && authHeader.startsWith("Bearer "))
            return authHeader.substring(7);
        return null;
    }

    public String getEmailFromToken(String token) throws ExpiredJwtException {
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            if (claims != null)
                return claims.getSubject();
        } catch (Exception ignored) {}
        return null;
    }

    public void getIssuedAtDateFromToken(String token) throws ExpiredJwtException {
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            if (claims != null)
                claims.getIssuedAt();
        } catch (Exception ignored) {}
    }

    public Date getExpirationDateFromToken(String token) throws ExpiredJwtException {
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            if (claims != null)
                return claims.getExpiration();
        } catch (Exception ignored) {}
        return null;
    }

    private Claims getAllClaimsFromToken(String token) throws ExpiredJwtException {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = getEmailFromToken(token);
        getIssuedAtDateFromToken(token);
        return (email != null && email.equals(userDetails.getUsername())); // getUsername zapravo dobavlja email
    }

    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }

    public int getExpiredIn() {
        return EXPIRES_IN;
    }
}