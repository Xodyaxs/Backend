package libreria.ms_usuarios.security;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

    private static final Duration TOKEN_LIFETIME = Duration.ofHours(2);
    private final SecretKey signingKey;

    public JwtService(@Value("${app.jwt.secret}") String secret) {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalArgumentException("JWT_SECRET debe tener al menos 32 bytes para HS256");
        }
        this.signingKey = Keys.hmacShaKeyFor(secretBytes);
    }

    public String generarToken(Long id, String email, String rol) {
        Instant issuedAt = Instant.now();
        return Jwts.builder()
            .subject(email)
            .claim("id", id)
            .claim("email", email)
            .claim("rol", rol)
            .issuedAt(Date.from(issuedAt))
            .expiration(Date.from(issuedAt.plus(TOKEN_LIFETIME)))
            .signWith(signingKey)
            .compact();
    }
}
