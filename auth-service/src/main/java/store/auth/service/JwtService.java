package store.auth.service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import store.auth.client.dto.AccountResponse;
import store.auth.exception.InvalidTokenException;

@Service
public class JwtService {

    private final Key signingKey;
    private final long duration;

    public JwtService(
        @Value("${store.jwt.secret}") String secret,
        @Value("${store.jwt.duration}") long duration
    ) {
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.duration = duration;
    }

    public String generate(AccountResponse account) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(duration);
        return Jwts.builder()
            .id(account.id().toString())
            .subject(account.name())
            .claim("email", account.email())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiry))
            .signWith(signingKey)
            .compact();
    }

    public UUID getId(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return UUID.fromString(claims.getId());
        } catch (RuntimeException exception) {
            throw new InvalidTokenException();
        }
    }
}
