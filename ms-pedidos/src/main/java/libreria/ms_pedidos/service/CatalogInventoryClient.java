package libreria.ms_pedidos.service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import libreria.ms_pedidos.dto.CrearPedidoRequest;

@Component
public class CatalogInventoryClient {

    private static final Duration SERVICE_TOKEN_LIFETIME = Duration.ofSeconds(60);

    private final RestClient restClient;
    private final SecretKey signingKey;

    public CatalogInventoryClient(
            @Value("${CATALOGO_URI:http://localhost:8081}") String catalogUrl,
            @Value("${app.jwt.secret}") String secret) {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalArgumentException("JWT_SECRET debe tener al menos 32 bytes para HS256");
        }
        this.restClient = RestClient.builder().baseUrl(catalogUrl).build();
        this.signingKey = Keys.hmacShaKeyFor(secretBytes);
    }

    public void reservar(List<CrearPedidoRequest.ItemRequest> items) {
        post("/internal/inventory/reserve", items);
    }

    public void liberar(List<CrearPedidoRequest.ItemRequest> items) {
        post("/internal/inventory/release", items);
    }

    private void post(String path, List<CrearPedidoRequest.ItemRequest> items) {
        InventoryAdjustmentRequest request = new InventoryAdjustmentRequest(
            items.stream()
                .map(item -> new InventoryAdjustmentRequest.Item(item.titulo(), item.cantidad()))
                .toList()
        );

        try {
            restClient.post()
                .uri(path)
                .header("Authorization", "Bearer " + createServiceToken())
                .body(request)
                .retrieve()
                .toBodilessEntity();
        } catch (RestClientResponseException exception) {
            throw new InventoryClientException(
                exception.getStatusCode(),
                "El catálogo rechazó la actualización del inventario",
                exception
            );
        } catch (RestClientException exception) {
            throw new InventoryClientException(
                HttpStatus.BAD_GATEWAY,
                "No se pudo contactar el servicio de catálogo",
                exception
            );
        }
    }

    private String createServiceToken() {
        Instant issuedAt = Instant.now();
        return Jwts.builder()
            .subject("ms-pedidos")
            .claim("rol", "INVENTORY")
            .issuedAt(Date.from(issuedAt))
            .expiration(Date.from(issuedAt.plus(SERVICE_TOKEN_LIFETIME)))
            .signWith(signingKey)
            .compact();
    }
}
