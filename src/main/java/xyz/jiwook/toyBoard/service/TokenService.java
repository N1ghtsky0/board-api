package xyz.jiwook.toyBoard.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class TokenService {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    private final RefreshTokenRepo refreshTokenRepo;

    public String generateAccessToken(String username) {
        return generateToken(username, 1000L * 60 * 30);
    }

    public String generateRefreshToken(String username, String ipAddress) {
        String refreshToken = generateToken("refreshToken", 1000L * 60 * 60 * 24 * 7);
        refreshTokenRepo.save(new RefreshTokenEntity(refreshToken, ipAddress, username));
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

    public String ExtractSubjectFromToken(String token) {
        return this.extractClaim(token, Claims::getSubject);
    }

    public Date ExtractExpirationFromToken(String token) {
        return this.extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
}
