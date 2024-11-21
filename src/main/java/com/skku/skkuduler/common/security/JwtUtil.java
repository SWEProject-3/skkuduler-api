package com.skku.skkuduler.common.security;

import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.domain.user.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.skku.skkuduler.common.exception.Error;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {


    private final SecretKey secretKey;
    private final Long expiredMs = 60 * 60 * 60 * 60 * 60 * 100L;

    public JwtUtil(@Value("${spring.jwt.secret}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public void validateToken(String bearer){
        try {
            String token = bearer.split(" ")[1];
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        }catch (SecurityException | MalformedJwtException e){
            throw new ErrorException(Error.INVALID_TOKEN);
        }catch (ExpiredJwtException e){
            throw new ErrorException(Error.EXPIRED_TOKEN);
        }catch (Exception e){
            throw new ErrorException(Error.WRONG_TOKEN);
        }
    }

    public Long extractUserId(String bearer) {
        String token = bearer.split(" ")[bearer.split(" ").length - 1];
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", Long.class);
    }

    public User.Role extractRole(String bearer){
        String token = bearer.split(" ")[bearer.split(" ").length - 1];
        String role = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
        System.out.println("role = " + role);
        return User.Role.valueOf(role);
    }

    public String generateToken(Long userId, User.Role role) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("role",role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }


}