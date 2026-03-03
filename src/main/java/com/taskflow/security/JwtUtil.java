package com.taskflow.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component  // Spring manages this — injectable everywhere
public class JwtUtil {

    @Value("${jwt.secret}")           // reads from application.properties
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // ── Generate Token ────────────────────────────────────────
    public String generateToken(String email, String role) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);     // add role as a custom claim

        return Jwts.builder()
                .claims(claims)
                .subject(email)                         // who this token is for
                .issuedAt(new Date())                   // when created
                .expiration(new Date(                   // when it expires
                        System.currentTimeMillis() + expiration
                ))
                .signWith(getSigningKey())              // sign with our secret
                .compact();                             // build the string
    }

    // ── Validate Token ────────────────────────────────────────
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);   // if this doesn't throw — token is valid
            return true;
        } catch (Exception e) {
            return false;       // expired, tampered, malformed — all invalid
        }
    }

    // ── Extract Email ─────────────────────────────────────────
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // ── Extract Role ──────────────────────────────────────────
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // ── Extract Expiration ────────────────────────────────────
    public Date extractExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    // ── Private Helpers ───────────────────────────────────────

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())    // verify signature first
                .build()
                .parseSignedClaims(token)
                .getPayload();                 // only reached if valid
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }
}