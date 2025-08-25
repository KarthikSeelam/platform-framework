package com.ican.cortex.platform.security.service;

import com.ican.cortex.platform.logger.base.APILogger;
import com.ican.cortex.platform.logger.constants.TransactionType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    // JWT expiration in milliseconds (e.g., 10 hours)
    @Value("${jwt.expirationInMs}")
    private long jwtExpirationInMs;

    @Value("${jwt.refreshExpirationInMs}")
    private long jwtRefreshExpirationInMs;

    @Autowired
    private APILogger apiLogger;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Key signingKey = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Map<String, Object> generateTokenResponse(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs); // Access token expiration
        Date refreshExpiryDate = new Date(now.getTime() + jwtRefreshExpirationInMs); // Refresh token expiration
        // Generate tokens
        String accessToken = createToken(username, now, expiryDate);
        String refreshToken = createRefreshToken(username, now, refreshExpiryDate);

        // Prepare response with access token, refresh token, and expiry details
        Map<String, Object> tokenResponse = new HashMap<>();
        tokenResponse.put("access_token", accessToken);
        tokenResponse.put("refresh_token", refreshToken);
        tokenResponse.put("token_type", "Bearer");
        tokenResponse.put("expires_in", jwtExpirationInMs / 1000); // in seconds
        tokenResponse.put("refresh_expires_in", jwtRefreshExpirationInMs / 1000); // refresh expiry in seconds

        return tokenResponse;
    }

    // Generate access token with expiration date
    private String createToken(String subject, Date issuedAt, Date expiration) {
        Key signingKey = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .claim("token", SECRET_KEY)
                .setExpiration(expiration)
                .signWith(signingKey, SignatureAlgorithm.HS256)// HMAC-SHA256 signing
                .compact();
    }

    // Generate refresh token with expiration date
    private String createRefreshToken(String subject, Date issuedAt, Date expiration) {
        Key signingKey = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(signingKey, SignatureAlgorithm.HS256)  // HMAC-SHA256 signing
                .compact();
    }


    public boolean validateAccessToken(String token) {
        try {
            Key signingKey = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            return true; // Token is valid
        } catch (Exception e) {
            apiLogger.error(null, "Token validation failed", e);
            return false; // Token is invalid
        }
    }


    public Boolean validateToken(String token, String userName) {
        final String tokenUsername = extractUserName(token);
        return (tokenUsername.equals(userName) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
