package com.system.personalfinance.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.system.personalfinance.dtos.auth.AuthResponse;
import com.system.personalfinance.dtos.auth.LoginRequest;
import com.system.personalfinance.dtos.auth.RefreshRequest;
import com.system.personalfinance.dtos.auth.RegisterRequest;
import com.system.personalfinance.entities.RefreshToken;
import com.system.personalfinance.entities.User;
import com.system.personalfinance.exceptions.ConflictException;
import com.system.personalfinance.repositories.RefreshTokenRepository;
import com.system.personalfinance.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final int refreshDays;

    public AuthService(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            @Value("${security.jwt.refresh-token-days}") int refreshDays
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshDays = refreshDays;
    }

    /**
     * Registra um novo usuário:
     * - valida e-mail único
     * - salva senha com hash (BCrypt)
     * - retorna accessToken e refreshToken
     */
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new ConflictException("E-mail já cadastrado");
        }

        String hash = passwordEncoder.encode(req.getPassword());
        User user = new User(req.getName(), req.getEmail(), hash);
        user = userRepository.save(user);

        return issueTokens(user);
    }

    /**
     * Realiza login:
     * - encontra usuário pelo e-mail
     * - valida senha com PasswordEncoder
     * - retorna accessToken e refreshToken
     */
    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciais inválidas");
        }

        return issueTokens(user);
    }

    /**
     * Gera tokens para um usuário:
     * - cria JWT (access)
     * - cria refresh token aleatório e salva apenas o hash no banco
     * - retorna accessToken e refreshToken (cru)
     */
    private AuthResponse issueTokens(User user) {
        String access = jwtService.generateAccessToken(user.getId(), user.getEmail());

        String refreshRaw = UUID.randomUUID().toString() + "." + UUID.randomUUID().toString();
        String refreshHash = sha256(refreshRaw);

        Instant expiresAt = Instant.now().plus(refreshDays, ChronoUnit.DAYS);
        refreshTokenRepository.save(new RefreshToken(user, refreshHash, expiresAt));

        return new AuthResponse(access, refreshRaw);
    }

    /**
     * Gera hash SHA-256 de uma string (usado para armazenar refresh token com segurança).
     */
    private String sha256(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao gerar hash", e);
        }
    }

    @Transactional
    public AuthResponse refresh(RefreshRequest req) {
        String refreshRaw = req.getRefreshToken();
        String refreshHash = sha256(refreshRaw);

        RefreshToken token = refreshTokenRepository.findByTokenHasWithUser(refreshHash)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token inválido"));

        if (token.getExpiresAt().isBefore(Instant.now())) {
            // remove token expirado do banco
            refreshTokenRepository.deleteByTokenHash(refreshHash);
            throw new IllegalArgumentException("Refresh token expirado");
        }

        User user = token.getUser();

        // ROTACIONAR refresh token:
        // apaga o antigo e gera um novo par
        refreshTokenRepository.deleteByTokenHash(refreshHash);

        return issueTokens(user);
    }
}
