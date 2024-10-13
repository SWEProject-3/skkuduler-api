package com.skku.skkuduler.common.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {


    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // JWT 생성
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .claim("email",email)
                .claim("role",role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // 10시간 유효
                .signWith(secretKey)
                .compact();
    }

    // JWT에서 이메일 추출
    public String extractEmail(String token) {
        String[] tokenized = token.split(" ");
        if(tokenized.length > 1 && tokenized[0].equals("Bearer"))  token = tokenized[1];
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public String extractRole(String token){
        String[] tokenized = token.split(" ");
        if(tokenized.length > 1 && tokenized[0].equals("Bearer")) token = tokenized[1];
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isTokenExpired(String token) {
        String[] tokenized = token.split(" ");
        if(tokenized.length > 1 && tokenized[0].equals("Bearer")) token = tokenized[1];
        boolean isExpired;
        try {
            isExpired = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
        }catch (ExpiredJwtException e) {
            return true;
        }
        return isExpired;
    }

}
