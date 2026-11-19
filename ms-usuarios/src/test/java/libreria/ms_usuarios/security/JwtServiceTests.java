package libreria.ms_usuarios.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;

class JwtServiceTests {

    private static final String SECRET = "TestSecretMustBeAtLeast32BytesLong!";

    @Test
    void generarTokenIncluyeLosDatosDelUsuarioYVencimiento() {
        JwtService jwtService = new JwtService(SECRET);
        String token = jwtService.generarToken(12L, "cliente@example.com", "CLIENTE");

        var claims = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
            .build()
            .parseSignedClaims(token)
            .getPayload();

        assertThat(claims.getSubject()).isEqualTo("cliente@example.com");
        assertThat(claims.get("id", Number.class).longValue()).isEqualTo(12L);
        assertThat(claims.get("email", String.class)).isEqualTo("cliente@example.com");
        assertThat(claims.get("rol", String.class)).isEqualTo("CLIENTE");
        assertThat(claims.getExpiration()).isAfter(claims.getIssuedAt());
    }

    @Test
    void rechazaUnaClaveDemasiadoCorta() {
        assertThatThrownBy(() -> new JwtService("short"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("JWT_SECRET");
    }
}
