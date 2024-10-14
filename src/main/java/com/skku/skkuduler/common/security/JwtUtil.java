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
    public String generateToken(Long userId) {
        return Jwts.builder()
                .claim("userId",userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // 10시간 유효
                .signWith(secretKey)
                .compact();
    }
    public Long extractUserId(String token) {
        String[] tokenized = token.split(" ");
        if(tokenized.length > 1 && tokenized[0].equals("Bearer"))  token = tokenized[1];
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", Long.class);
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
