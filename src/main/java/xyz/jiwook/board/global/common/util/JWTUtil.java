package xyz.jiwook.board.global.common.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class JWTUtil {
    private final String SECRET_KEY;
    private final String ISSUER;

    public JWTUtil(@Value("${jwt.secret-key}") String secretKey,
                   @Value("${jwt.issuer}") String issuer) {
        this.SECRET_KEY = secretKey;
        this.ISSUER = issuer;
    }

    public String generateAccessToken(String subject) {
        HashMap<String, Object> claims = new HashMap<>();
        long now = System.currentTimeMillis();

        claims.put("sub", subject);
        claims.put("iss", ISSUER);
        claims.put("iat", now);
        claims.put("exp", now + 1000 * 60 * 30);

        return generateToken(claims);
    }

    public String generateRefreshToken() {
        HashMap<String, Object> claims = new HashMap<>();
        long now = System.currentTimeMillis();

        claims.put("sub", "refresh");
        claims.put("iat", now);
        claims.put("exp", now + (long) 1000 * 60 * 60 * 24 * 30);

        return generateToken(claims);
    }

    private String generateToken(HashMap<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY)))
                .compact();
    }
}
