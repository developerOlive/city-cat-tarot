package com.cityCatTarot.utils;

import com.cityCatTarot.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * JWT 토큰을 관리합니다.
 */
@Component
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * JWT 토큰을 생성합니다.
     *
     * @param userId 회원 식별자
     * @return 생성된 토큰
     */
    public String encode(Long userId) {
        String token = Jwts.builder()
                .claim("user_id", userId)
                .signWith(key)
                .compact();

        return token;
    }

    /**
     * JWT 토큰을 복호화합니다.
     *
     * @param token 토큰
     * @return 복호화된 정보
     * @throws InvalidTokenException 토큰이 유효하지 않을 경우
     */
    public Claims decode(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException(token);
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new InvalidTokenException(token);
        }
    }
}
