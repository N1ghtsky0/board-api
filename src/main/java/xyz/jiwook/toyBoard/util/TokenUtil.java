package xyz.jiwook.toyBoard.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.jiwook.toyBoard.dao.RefreshTokenRepo;
import xyz.jiwook.toyBoard.entity.RefreshTokenEntity;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class TokenUtil {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    private final RefreshTokenRepo refreshTokenRepo;

    public String generateAccessToken(String username) {
        return generateToken(username, 1000L * 60 * 30);
    }

    public String generateRefreshToken(String username, String ipAddress) {
        String refreshToken = generateToken("refreshToken", 1000L * 60 * 60 * 24 * 7);
        refreshTokenRepo.save(new RefreshTokenEntity(refreshToken, username, ipAddress));
        return refreshToken;
    }

    private String generateToken(String subject, long expireTime) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expireTime))
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
}
