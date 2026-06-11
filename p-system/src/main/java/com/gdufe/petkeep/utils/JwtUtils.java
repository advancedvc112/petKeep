package com.gdufe.petkeep.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类
 * <p>
 * 配置项：jwt.secret / jwt.expire-hours（在 application.yml 中定义）
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret:pet-keep-system-jwt-secret-key-2026}")
    private String secret;

    @Value("${jwt.expire-hours:24}")
    private int expireHours;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 Token（负载：userId, role）
     */
    public String generateToken(Long userId, Integer role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expireHours * 3600_000L);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    /**
     * 解析 Token 中的 userId
     */
    public Long parseUserId(String token) {
        return Long.parseLong(parseClaims(token).getSubject());
    }

    /**
     * 解析 Token 中的 role
     */
    public Integer parseRole(String token) {
        return parseClaims(token).get("role", Integer.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
