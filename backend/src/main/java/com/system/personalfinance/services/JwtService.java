package com.system.personalfinance.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Serviço responsável por gerar JWTs (access tokens) do sistema.
 * O token inclui issuer, subject (userId), claims e expiração.
 */
@Service
public class JwtService {

    private final String issuer;
    private final int accessMinutes;
    private final String secret;

    public JwtService(
            @Value("${security.jwt.issuer}") String issuer,
            @Value("${security.jwt.access-token-minutes}") int accessMinutes,
            @Value("${security.jwt.secret}") String secret
    ) {
        this.issuer = issuer;
        this.accessMinutes = accessMinutes;
        this.secret = secret;
    }

    /**
     * Constrói a chave criptográfica (HMAC) a partir do secret configurado.
     * Essa chave é usada para assinar os tokens JWT.
     */
    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Gera um access token (JWT) para o usuário informado.
     * - subject: userId
     * - claim extra: email
     * - expira em X minutos (config)
     */
    public String generateAccessToken(Long userId, String email) {
        Instant now = Instant.now();
        Instant exp = now.plus(accessMinutes, ChronoUnit.MINUTES);

        return Jwts.builder()
                .issuer(issuer)
                .subject(String.valueOf(userId))
                .claim("email", email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key())
                .compact();
    }
}
