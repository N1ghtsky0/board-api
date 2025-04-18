package xyz.jiwook.board.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class TokenUtil {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    public String generateAccessToken(String username) {
        return generateToken(username, 1000 * 60 * 30);
    }

    public String generateRefreshToken() {
        return generateToken("refresh", 1000 * 60 * 60 * 24 * 7);
    }

    private String generateToken(String subject, long duration) {
        final long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date(now))
                .expiration(new Date(now + duration))
                .signWith(getSecretKey())
                .compact();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY));
    }
}
