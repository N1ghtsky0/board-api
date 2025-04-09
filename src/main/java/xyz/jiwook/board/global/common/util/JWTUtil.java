package xyz.jiwook.board.global.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JWTUtil {
    private final SecretKey SECRET_KEY;
    private final String ISSUER;

    public JWTUtil(@Value("${jwt.secret-key}") String secretKey,
                   @Value("${jwt.issuer}") String issuer) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
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
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
    }
}
